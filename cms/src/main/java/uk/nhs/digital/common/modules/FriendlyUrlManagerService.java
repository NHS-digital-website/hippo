package uk.nhs.digital.common.modules;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.rest.DocumentService;
import org.hippoecm.repository.HippoStdNodeType;
import org.hippoecm.repository.api.Document;
import org.hippoecm.repository.api.HippoNode;
import org.hippoecm.repository.util.JcrUtils;
import org.hippoecm.repository.util.NodeIterable;
import org.onehippo.cms7.services.eventbus.Subscribe;
import org.onehippo.forge.content.exim.core.DocumentManager;
import org.onehippo.forge.content.exim.core.DocumentManagerException;
import org.onehippo.forge.content.exim.core.util.ContentPathUtils;
import org.onehippo.repository.events.HippoWorkflowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Arrays;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class FriendlyUrlManagerService {

    private static final Logger log = LoggerFactory.getLogger(PublicationPostProcessingModule.class);

    public static final String PUBLICATION_INTERACTION = "default:handle:publish";
    public static final String TITLE_PROPERTY = "website:title";

    private final Session session;

    private final DocumentManager documentManager;

    private DocumentService documentService;

    public FriendlyUrlManagerService(final Session session,
                                     final DocumentManager documentManager) {
        this.session = session;
        this.documentManager = documentManager;
    }

    public void setDocumentService(final DocumentService documentService) {
        this.documentService = documentService;
    }

    @Subscribe
    public void handleEvent(final HippoWorkflowEvent event) {
        if (documentService != null && event.success() && PUBLICATION_INTERACTION.equals(event.interaction())) {
            postPublish(event);
        }
    }

    private void postPublish(final HippoWorkflowEvent workflowEvent) {
        String publishedDocumentTitle = null;
        try {
            final HippoNode handle = (HippoNode) session.getNodeByIdentifier(workflowEvent.subjectId());
            final Node published = getPublishedVariant(handle);
            if (published != null) {

                Node friendlyUrlsCompound =
                    JcrUtils.getNodeIfExists(published, "website:friendlyurls");
                //check if the document contains the friendlyurls compound as child node
                if (friendlyUrlsCompound != null) {
                    String[] alternativeUrls = JcrUtils.getMultipleStringProperty(friendlyUrlsCompound, "website:alternativeurls", ArrayUtils.EMPTY_STRING_ARRAY);
                    //if the alternative urls field contains at least one item
                    if (alternativeUrls.length > 0) {
                        publishedDocumentTitle = JcrUtils.getStringProperty(published, TITLE_PROPERTY, handle.getDisplayName());
                        //determine the source document handle node path.
                        String publishedDocumentUuid = published.getIdentifier();
                        //copying the locale of the existing document
                        String locale = published.hasProperty("hippotranslation:locale") ? published.getProperty("hippotranslation:locale").getValue().getString() : null;
                        // determine the location of the url rewrite to create/update
                        String rewriteRuleDocumentLocation = "/content/urlrewriter/rules/" + publishedDocumentUuid.replace("-", "/");
                        //check if the mapped the rewrite rules exists: if it doesn't then create it
                        if (!documentManager.documentExists(rewriteRuleDocumentLocation)) {
                            rewriteRuleDocumentLocation = StringUtils
                                .removeEnd(rewriteRuleDocumentLocation, "/");
                            //splitting folder path and document name
                            String[] folderPathAndName = ContentPathUtils.splitToFolderPathAndName(rewriteRuleDocumentLocation);
                            //create url rewrite rule
                            documentManager.createDocument(folderPathAndName[0], "urlrewriter:advancedrule",
                                folderPathAndName[1], locale, publishedDocumentTitle);
                            log.debug("Url rewrite rule created here {}", rewriteRuleDocumentLocation);
                        }

                        Document editableDocument = null;
                        Node variant = null;
                        try {
                            editableDocument = documentManager.obtainEditableDocument(rewriteRuleDocumentLocation);
                            variant = editableDocument.getCheckedOutNode(documentManager.getSession());
                            //filling a basic description
                            variant.setProperty("urlrewriter:ruledescription", "This rule has been automatically created/updated while saving " + published.getPath());
                            //setting the type
                            String ruleType = JcrUtils.getStringProperty(variant, "urlrewriter:ruletype", "");
                            if (StringUtils.isEmpty(ruleType)) {
                                //setting a temporary redirect by default
                                variant.setProperty("urlrewriter:ruletype", "temporary-redirect");
                            }
                            //joining all the values coming from the alternativeUrls compound
                            String[] filteredArray =
                                Arrays.stream(alternativeUrls).filter(s -> StringUtils.isNotBlank(s)).toArray(size -> new String[size]);
                            variant.setProperty("urlrewriter:rulefrom", "(" + String.join("|", filteredArray) + ")");
                            //using the document service to get the canonical url
                            String documentUrl = documentService
                                .getUrl(handle.getIdentifier(), "live");
                            log.debug("Canonical url generated for the document {} is {}", handle.getPath(), documentUrl);
                            //The documentUrl is including the context path only IF the showcontextpath property is enabled in the hst hosts configuration
                            variant.setProperty("urlrewriter:ruleto", new URI(documentUrl).getPath());
                            //getContentNodeBinder().bind(variant, contentNode, getContentNodeBindingItemFilter(), getContentValueConverter());
                            documentManager.getSession().save();
                            log.debug("Document {} has been saved correctly", rewriteRuleDocumentLocation);
                            documentManager.commitEditableDocument(rewriteRuleDocumentLocation);
                            log.debug("Document {} has been committed", rewriteRuleDocumentLocation);
                            documentManager.publishDocument(rewriteRuleDocumentLocation);
                            log.debug("Document {} has been published", rewriteRuleDocumentLocation);
                        } catch (DocumentManagerException | RepositoryException exception) {
                            log.warn("Something went wrong while editing this document {}: {}", rewriteRuleDocumentLocation, exception);
                            if (editableDocument != null) {
                                documentManager.disposeEditableDocument(rewriteRuleDocumentLocation);
                                log.debug("Document {} has been released", rewriteRuleDocumentLocation);
                            }
                            throw exception;
                        } catch (Exception genericException) {
                            log.warn("Some errors occurred, maybe while using the internal document restservices {}", genericException);
                            if (editableDocument != null) {
                                documentManager.disposeEditableDocument(rewriteRuleDocumentLocation);
                                log.debug("Document {} has been released", rewriteRuleDocumentLocation);
                            }
                        }
                    }
                }

            } else {
                log.warn("Something's wrong because I can't find the document variant that was just published");
                publishedDocumentTitle = handle.getDisplayName();
            }
        } catch (ItemNotFoundException itemNotFoundException) {
            log.warn("Something's wrong because I can't find the handle of the document that was just published");
        } catch (RepositoryException repositoryException) {
            log.error("Something's very wrong: unexpected exception while doing simple JCR read operations", repositoryException);
        }
        log.debug(workflowEvent.user() + " published " + publishedDocumentTitle);
    }

    private static Node getPublishedVariant(Node handle) throws RepositoryException {
        for (Node variant : new NodeIterable(handle.getNodes(handle.getName()))) {
            final String state = JcrUtils.getStringProperty(variant, HippoStdNodeType.HIPPOSTD_STATE, null);
            if (HippoStdNodeType.PUBLISHED.equals(state)) {
                return variant;
            }
        }
        return null;
    }


}
