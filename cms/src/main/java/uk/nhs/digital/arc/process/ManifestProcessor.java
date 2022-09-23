package uk.nhs.digital.arc.process;

import com.amazonaws.SdkClientException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.onehippo.forge.content.exim.core.impl.WorkflowDocumentVariantImportTask;
import org.onehippo.forge.content.exim.core.util.ContentPathUtils;
import org.onehippo.forge.content.pojo.model.ContentNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.arc.exception.ArcException;
import uk.nhs.digital.arc.factory.ArcTransformerFactory;
import uk.nhs.digital.arc.factory.JsonClassFactory;
import uk.nhs.digital.arc.json.ArcDoctype;
import uk.nhs.digital.arc.json.ManifestWrapper;
import uk.nhs.digital.arc.json.Page;
import uk.nhs.digital.arc.storage.ArcStorageManager;
import uk.nhs.digital.arc.transformer.abs.AbstractPageLevelTransformer;
import uk.nhs.digital.arc.util.FilePathData;
import uk.nhs.digital.arc.util.ManifestUtils;
import uk.nhs.digital.arc.util.PageUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.jcr.Session;

/**
 * This class is responsible for loading a manifest from a nominated location and processing it.
 * That processing can either be in a parsing capacity, where the syntax of the file and its segments
 * is validated, or it can the actual application of the data and creation of documents and nodes
 *
 * @author Ian Pearce
 */
public class ManifestProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManifestProcessor.class);
    public static final String LOGFILE_FOLDERNAME = "logs/";

    private final String manifestFile;
    private final String nodePath;
    private final boolean parsing;
    private final PageUtils pageUtils;
    private ObjectMapper objectMapper = null;

    private Session session = null;

    private ArcStorageManager storageManager;

    private ArcTaskManager arcTaskManager;

    public ManifestProcessor(boolean parsing, Session session, String manifestFile, String nodePath, ArcStorageManager storageManager, ArcTaskManager arcTaskManager) {
        this.session = session;
        this.manifestFile = manifestFile;
        this.nodePath = nodePath;
        this.parsing = parsing;
        this.storageManager = storageManager;
        this.pageUtils = new PageUtils(session, nodePath);
        this.arcTaskManager = arcTaskManager;
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
        messageSummary.setParsing(parsing);

        boolean fileExists = false;

        try {
            fileExists = storageManager.fileExists(manifestFile);
        } catch (SdkClientException s3) {
            fileExists = false;
        }

        // At this point, we only deal with files located in an S3 bucket
        if (manifestFileData.isS3Protocol() && fileExists) {
            InputStream in = storageManager.getFileInputStream(manifestFileData);

            configureObjectMapper();

            try {
                ManifestWrapper wrapper = objectMapper.readValue(in, ManifestWrapper.class);
                String calculatedDocbase = ManifestUtils.getDocbaseUsingManifestLocationIfRequired(wrapper.getDocumentBase(), manifestFileData);

                if (calculatedDocbase == null || wrapper.getPages() == null || wrapper.getPages().size() == 0) {
                    ProcessOutcome outcome;
                    if (calculatedDocbase == null) {
                        outcome = new ProcessOutcome("Could not locate a docbase in the file '" + manifestFile + "'", ProcessOutcome.ERROR);
                    } else {
                        messageSummary.setDocbase(calculatedDocbase);
                        messageSummary.setOutputFileLocation(LOGFILE_FOLDERNAME + ManifestUtils.getOutputFileLocation(Instant.now(), getProcessorModeAsText()));
                        outcome = new ProcessOutcome("Could not locate any page sections in the file '" + manifestFile + "'", ProcessOutcome.ERROR);
                    }
                    messageSummary.addIndividualProcessOutcome(outcome);
                } else {
                    messageSummary.setDocbase(calculatedDocbase);
                    messageSummary.setOutputFileLocation(LOGFILE_FOLDERNAME + ManifestUtils.getOutputFileLocation(Instant.now(), getProcessorModeAsText()));

                    List<String> existingPages = new ArrayList<>();
                    List<String> createdPages = null;

                    if (!parsing) {
                        //* Only need toknow what's already there if we may need to adjust (in create mode) later
                        existingPages = pageUtils.getExistingPages();
                    }

                    createdPages = processEachPageInWrapper(wrapper, messageSummary, calculatedDocbase);

                    if (!parsing) {
                        //* Only need to adjust if in create mode
                        pageUtils.removeOrphanedPages(existingPages, createdPages);
                    }
                }
            } catch (IOException | SdkClientException io) {
                messageSummary.addIndividualProcessOutcome(new ProcessOutcome("Problem reading the manifest file in the location '" + manifestFile + "'."
                    + " Check that it exists, is correctly named and whether you have access",
                    ProcessOutcome.ERROR));
                messageSummary.addIndividualProcessOutcome(new ProcessOutcome("Error message returned was: " + io.getMessage(),
                    ProcessOutcome.ERROR));
            }
        } else {
            messageSummary.addIndividualProcessOutcome(new ProcessOutcome("Could not locate manifest file in the location: " + manifestFile + "."
                + " Check that it exists, is correctly named and whether you have access.",
                ProcessOutcome.ERROR));
            messageSummary.addIndividualProcessOutcome(new ProcessOutcome(" Additionally, because of this, no outcome file could be created",
                ProcessOutcome.ERROR));
        }
        return messageSummary;
    }

    /**
     * Used for file naming and easier to calculate here than a horrible expression in line
     *
     * @return the type of processing, as a string
     */
    private String getProcessorModeAsText() {
        return parsing ? "parsing" : "creation";
    }

    /**
     * Process each page that we find referenced in the manifest wrapper.
     * For each page we create, we store the reference and pass that back as part of a list
     *
     * @param wrapper is the {@link ManifestWrapper} that refers to the pages. At this point, we know there is at least one page
     * @param messageSummary is the message wrapping object for the outcome of each page being processed
     * @param calculatedDocbase is used to get the absolute location of wrapper page information
     *
     * @return the list of pages that we have been able to create
     */
    private List<String>  processEachPageInWrapper(ManifestWrapper wrapper, ManifestProcessingSummary messageSummary, String calculatedDocbase) {
        List<String> createdPages = new ArrayList<>();

        String reportRootFolder = pageUtils.getReportRootFolder();

        wrapper.getPages().stream().forEach(page -> {
            try {
                messageSummary.addIndividualProcessOutcome(loadAndProcessPage(calculatedDocbase, page, objectMapper));
                createdPages.add(reportRootFolder + "/" + page.getPageName());
            } catch (IOException e) {
                LOGGER.error("Error processing the individual page", e);
            }
        });
        return createdPages;
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

        ProcessOutcome processOutcome = new ProcessOutcome("\nNow checking the manifest segment '"
            + page.getPageRef()
            + "', which is used by the page referenced as '"
            + page.getPageName() + "' in the main manifest ...\n");

        // We only deal with S3 objects that exist at the moment
        if (filePathData.isS3Protocol() && storageManager.fileExists(filePathData)) {
            InputStream in = storageManager.getFileInputStream(filePathData);

            try {
                final Class jsonDataClass = JsonClassFactory.getJsonDataClassFromDocumentType(page.getDocumentType());
                final ArcDoctype jsonObject = (ArcDoctype) objectMapper.readValue(in, jsonDataClass);

                ManifestUtils.checkValidityOfUrls(storageManager, docbase, jsonObject.getAllReferencedExternalUrls(), processOutcome);
                addPublicationSystemDocument(docbase, page.getDocumentType(), page.getPageName(), jsonObject);

                if (!processOutcome.isInError()) {
                    processOutcome.addMessageLine("... parsed OK\n\n");
                } else {
                    processOutcome.addErrorMessageLine("... errors above will stop processing from continuing. Please adjust file locations where necessary\n\n");
                }
            } catch (Exception e) {
                processOutcome.addErrorMessageLine("\n*** Error encountered during parsing:\n");
                processOutcome.addStackTrace(e);
                processOutcome.addIndentedErrorMessageLine(e.getMessage() + "\n");
            }
        } else {
            processOutcome.addErrorMessageLine("** Could not find the manifest segment '" + filePathData.getFilePath()
                + "' which is referenced in the main manifest as '" + page.getPageName()
                + "' - check docbase and filename form a valid path\n");
        }
        return processOutcome;
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
    private void addPublicationSystemDocument(String docbase, String documentType, String pageName, ArcDoctype arcDocument) throws ArcException {
        WorkflowDocumentVariantImportTask importTask = null;

        if (!parsing) {
            importTask = arcTaskManager.getImportTask();

            importTask.setContentNodeBinder(new ArcJcrContentNodeBinder());

            LOGGER.debug("Automatic Report Creation process is now starting for the page {}", pageName);
            importTask.setLogger(LOGGER);
            importTask.start();
        }

        AbstractPageLevelTransformer transformer = ArcTransformerFactory.getArcTransformerFromDocumentType(documentType);
        transformer.setDocbase(docbase);
        transformer.setDoctype(arcDocument);
        transformer.setSession(session);
        transformer.setStorageManager(storageManager);

        ContentNode contentNode = null;

        try {
            contentNode = transformer.process();
        } catch (ArcException | RuntimeException exception) {
            LOGGER.error("For the page {}, this process failed. {}", pageName, exception);
            //exception.printStackTrace();
            throw exception;
        }

        if (!parsing && contentNode != null) {
            String localizedName = arcDocument.getTitleReq();
            contentNode.setName(localizedName);

            String[] paths = ContentPathUtils.splitToFolderPathAndName(nodePath);
            importTask.createOrUpdateDocumentFromVariantContentNode(contentNode, contentNode.getPrimaryType(), paths[0] + "/" + pageName, null, localizedName);
            ArcJcrContentNodeBinder binder = (ArcJcrContentNodeBinder)importTask.getContentNodeBinder();
            for (ExternalStorageReference reference: binder.getExternalStorageReferences()) {
                LOGGER.info("For the page {}, this file {} is no longer in use", pageName, reference);
            }
        }

        LOGGER.debug("Automatic Report Creation process has now completed for the page {}", pageName);
    }

    /**
     * Initialise the ObjectMapper.
     * This is only called once we know we can process a file
     */
    private void configureObjectMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
