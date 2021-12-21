package uk.nhs.digital.arc.process;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.onehippo.forge.content.exim.core.DocumentManager;
import org.onehippo.forge.content.exim.core.impl.WorkflowDocumentManagerImpl;
import org.onehippo.forge.content.exim.core.impl.WorkflowDocumentVariantImportTask;
import org.onehippo.forge.content.exim.core.util.ContentPathUtils;
import org.onehippo.forge.content.pojo.model.ContentNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.arc.factory.ArcTransformerFactory;
import uk.nhs.digital.arc.factory.JsonClassFactory;
import uk.nhs.digital.arc.json.ArcDoctype;
import uk.nhs.digital.arc.json.ManifestWrapper;
import uk.nhs.digital.arc.json.Page;
import uk.nhs.digital.arc.storage.ArcStorageManager;
import uk.nhs.digital.arc.storage.S3StorageManager;
import uk.nhs.digital.arc.transformer.abs.AbstractPageLevelTransformer;
import uk.nhs.digital.arc.util.FilePathData;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * This class is responsible for loading a manifest from a nominated location and processing it.
 * That processing can either be in a preview capacity, where the syntax of the file and its segments
 * is validated, or it can the actual application of the data and creation of documents and nodes
 *
 * @author Ian Pearce
 */
public class ManifestProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManifestProcessor.class);

    private final String manifestFile;
    private final String nodePath;
    private final boolean preview;

    private Session session = null;

    private ArcStorageManager storageManager;

    public ManifestProcessor(Session session, String manifestFile, String nodePath) {
        this.session = session;
        this.manifestFile = manifestFile;
        this.nodePath = nodePath;
        this.preview = session == null;
        this.storageManager = new S3StorageManager();
    }

    public ManifestProcessor(Session session, String manifestFile, String nodePath, ArcStorageManager storageManager) {
        this.session = session;
        this.manifestFile = manifestFile;
        this.nodePath = nodePath;
        this.preview = session == null;
        this.storageManager = storageManager;
    }

    /**
     * The wrapper is the main manifest and contains references to documents that we want to create
     * It also has a docbase which is used when referencing files that are not fully qualified inside the segment data
     * @return is a {@link ManifestProcessingSummary} with details of all the pages created along with any error conditions
     * that have been encountered
     * @throws IOException if we are unable to locate the manifest
     */
    public ManifestProcessingSummary readWrapperFromFile() throws IOException {
        FilePathData manifestFileData = new FilePathData(manifestFile);
        ManifestProcessingSummary messageSummary = new ManifestProcessingSummary();

        // At this point, we only deal with files located in an S3 bucket
        if (manifestFileData.isS3Protocol() && storageManager.fileExists(manifestFile)) {
            InputStream in = storageManager.getFileInputStream(manifestFileData);
            List<String> createdPages = new ArrayList<>();
            List<String> existingPages = new ArrayList<>();

            String reportRootFolder = getReportRootFolder();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            ManifestWrapper wrapper = objectMapper.readValue(in, ManifestWrapper.class);

            // Preview mode does validation of new items but non-preview will potentially
            // adjust the pages and their order so we want to capture what we have before we
            // start adjusting it
            if (!preview) {
                existingPages = getExistingPages();
            }

            String calculatedDocbase = getDocbaseUsingManifestLocationIfRequired(wrapper.getDocumentBase(), manifestFileData);

            if (calculatedDocbase == null || wrapper.getPages() == null || wrapper.getPages().size() == 0) {
                ProcessOutcome outcome;
                if (calculatedDocbase == null) {
                    outcome = new ProcessOutcome("Could not locate a docbase in the file: " + manifestFile, ProcessOutcome.ERROR);
                } else {
                    outcome = new ProcessOutcome("Could not locate any page sections in the file: " + manifestFile, ProcessOutcome.ERROR);
                }
                messageSummary.addIndividualProcessOutcome(outcome);
            } else {
                // FOr each page, we send it to the processing method and record the outcome
                wrapper.getPages().stream().forEach(page -> {
                    try {
                        messageSummary.addIndividualProcessOutcome(loadAndProcessPage(calculatedDocbase, page, objectMapper));
                        createdPages.add(reportRootFolder + "/" + page.getPageName());
                    } catch (IOException e) {
                        LOGGER.error("Error processing the individual page", e);
                    }
                });

                // Remembering that we might be changing things when properly processing data, we need to adjust
                // the contents of the folder and remove pages that we no longer need as well as removing
                // files from S3 that are no longer referenced (since their referencing JCR nodes wil have long been deleted)
                if (!preview) {
                    removeOrphanedPages(existingPages, createdPages);
                }
            }
        } else {
            messageSummary.addIndividualProcessOutcome(new ProcessOutcome("Could not locate manifest file in the location: " + manifestFile,
                ProcessOutcome.ERROR));
        }
        return messageSummary;
    }

    /**
     * A document base can be supplied as a fully qualified location or as a relative location.
     * If it's relative, we'll use teh manifest location as a root
     *
     * @param documentBase which may not bea fully qualified URL
     * @param manifestFileData which will be a fully qualified location
     * @return a fully qualified location for the docbase
     */
    private String getDocbaseUsingManifestLocationIfRequired(String documentBase, FilePathData manifestFileData) {
        FilePathData docbaseData = new FilePathData(documentBase);

        if (documentBase == null) {
            return null;
        }

        // If not a S3 reference (or HTTP or HTTPS) then assume a relative location
        if (!docbaseData.looksLikeAUrl()) {
            return "s3://" + manifestFileData.getS3Bucketname() + "/" + manifestFileData.getFolderPathNoBucket() + "/" + documentBase;
        }

        return documentBase;
    }

    private void removeOrphanedPages(List<String> existingPages, List<String> createdPages) {
        Set<String> removed = Sets.difference(new HashSet<>(existingPages), new HashSet<>(createdPages));
        removed.stream().forEach(pageName -> {
            LOGGER.debug("Following orphaned page found in ARC will be removed from the folder: {}", pageName);
            new WorkflowDocumentManagerImpl(session).deleteDocument(pageName);
        });
    }

    /**
     * Find all pages currently attributed to this report so taht we know what we have before we start
     *
     * @return the {@link List} of content node paths
     */
    private List<String> getExistingPages() {
        List<String> createdPages = new ArrayList<>();

        Node folderNode = null;
        try {
            folderNode = session.getNode(getReportRootFolder());
            NodeIterator pages = folderNode.getNodes();

            while (pages.hasNext()) {
                Node pageNode = pages.nextNode();
                createdPages.add(pageNode.getPath());
            }
        } catch (RepositoryException e) {
            LOGGER.error("Error trying to read information about existing pages in the report", e);
        }
        return createdPages;
    }

    private String getReportRootFolder() {
        String[] paths = ContentPathUtils.splitToFolderPathAndName(nodePath);
        return paths[0];
    }

    /**
     * Each segment is processed here and the outcome recorded
     * @param docbase the base for all S3 files and the manifest itself
     * @param page the item we are about to process
     * @param objectMapper the object mapper is used to transform json data into objects. It's useful to keep one instance of this only
     * @return the outcome of the process
     * @throws IOException should we encounter an exception during processing
     */
    private ProcessOutcome loadAndProcessPage(String docbase, Page page, ObjectMapper objectMapper) throws IOException {
        FilePathData filePathData = new FilePathData(docbase, page.getPageRef());

        ProcessOutcome processOutcome = new ProcessOutcome("Now checking the manifest segment '"
            + page.getPageRef()
            + "', which is used by the page '"
            + page.getPageName() + "' ...\n");

        // We only deal with S3 objects that exist at the moment
        if (filePathData.isS3Protocol() && storageManager.fileExists(filePathData)) {
            InputStream in = storageManager.getFileInputStream(filePathData);

            try {
                final Class jsonDataClass = JsonClassFactory.getJsonDataClassFromDocumentType(page.getDocumentType());
                final ArcDoctype jsonObject = (ArcDoctype) objectMapper.readValue(in, jsonDataClass);

                if (!preview) {
                    addPublicationSystemDocument(docbase, page.getDocumentType(), page.getPageName(), jsonObject);
                } else {
                    checkValidityOfUrls(docbase, jsonObject.getAllReferencedExternalUrls(), processOutcome);
                }

                if (!processOutcome.isInError()) {
                    processOutcome.addMessageLine("... parsed OK\n\n");
                } else {
                    processOutcome.addErrorMessageLine("... errors above will stop processing from continuing. Please adjust file locations where necessary\n\n");
                }
            } catch (Exception e) {
                processOutcome.addErrorMessageLine("\nError encountered during parsing:\n");
                processOutcome.addErrorMessageLine(ExceptionUtils.getStackTrace(e) + "\n");
            }
        } else {
            processOutcome.addErrorMessageLine("** Could not find the segment: " + filePathData.getFilePath() + " - check docbase and filename form a valid path\n");
        }
        return processOutcome;
    }

    /**
     * Checks to see if a claimed file URL really does exist in S3
     * @param docbase teh base from which we take a reference
     * @param allReferencedExternalUrls the set of Urls that we have determined need to be checked
     * @param processOutcome the outcome of the checks on these files
     */
    private void checkValidityOfUrls(String docbase, List<String> allReferencedExternalUrls, ProcessOutcome processOutcome) {
        for (String referencedFile : allReferencedExternalUrls) {
            if (!storageManager.fileExists(new FilePathData(docbase, referencedFile))) {
                processOutcome.addIndentedErrorMessageLine("** Unable to find file '" + referencedFile + "' in the location you specified\n");
            } else {
                processOutcome.addIndentedMessageLine("File '" + referencedFile + "' found OK \n");
            }
        }
    }

    /**
     * This methiod does the work of creating and applying content based on a set of Json that will have been originally
     * provided in a segment and referenced by the manifest file.
     *
     * We're using the content export-import (EXIM) processing here (which is a BloomReach plugin) and which allows us to create
     * a set of {@link ContentNode}s that can be imported using the tasks defined in the EXIM library
     *
     * @param docbase the reference point for files
     * @param documentType the type of document, as defined in the set of BloomReach document types
     * @param pageName the name that the page wil have in the JCT repository (not the title)
     * @param arcDocument the set of data from which we wil pull values and create our {@link ContentNode}s
     * @return the set of files that have been added to the S3 destination bucket
     */
    private void addPublicationSystemDocument(String docbase, String documentType, String pageName, ArcDoctype arcDocument) {
        final DocumentManager documentManager = new WorkflowDocumentManagerImpl(session);
        final WorkflowDocumentVariantImportTask importTask = new WorkflowDocumentVariantImportTask(documentManager);
        importTask.setContentNodeBinder(new ArcJcrContentNodeBinder());

        LOGGER.debug("Automatic Report Creation process is now starting for the page {}", pageName);
        importTask.setLogger(LOGGER);
        importTask.start();

        AbstractPageLevelTransformer transformer = ArcTransformerFactory.getArcTransformerFromDocumentType(documentType);
        transformer.setDocbase(docbase);
        transformer.setDoctype(arcDocument);
        transformer.setSession(session);
        transformer.setStorageManager(storageManager);

        ContentNode contentNode = transformer.process();

        String localizedName = arcDocument.getTitleReq();
        contentNode.setName(localizedName);

        String[] paths = ContentPathUtils.splitToFolderPathAndName(nodePath);

        importTask.createOrUpdateDocumentFromVariantContentNode(contentNode, contentNode.getPrimaryType(), paths[0] + "/" + pageName, null, localizedName);

        ArcJcrContentNodeBinder binder = (ArcJcrContentNodeBinder)importTask.getContentNodeBinder();
        for (ExternalStorageReference reference: binder.getExternalStorageReferences()) {
            LOGGER.info("For the page {}, this file {} is no longer in use", pageName, reference);
        }
        LOGGER.debug("Automatic Report Creation process has now completed for the page {}", pageName);
    }
}
