package uk.nhs.digital.arc.transformer.impl.pagelevel;

import org.onehippo.forge.content.pojo.model.ContentNode;
import org.onehippo.forge.content.pojo.model.ContentPropertyType;
import uk.nhs.digital.arc.factory.InnerSectionTransformerFactory;
import uk.nhs.digital.arc.json.Publication;
import uk.nhs.digital.arc.json.PublicationBodyItem;
import uk.nhs.digital.arc.json.publicationsystem.PublicationsystemChangeNotice;
import uk.nhs.digital.arc.json.publicationsystem.PublicationsystemExternalattachment;
import uk.nhs.digital.arc.json.publicationsystem.PublicationsystemInteractivetool;
import uk.nhs.digital.arc.json.publicationsystem.PublicationsystemResourceOrExternalLink;
import uk.nhs.digital.arc.transformer.abs.AbstractPageLevelTransformer;
import uk.nhs.digital.arc.transformer.abs.AbstractSectionTransformer;
import uk.nhs.digital.arc.transformer.abs.AbstractTransformer;
import uk.nhs.digital.arc.transformer.impl.website.InfographicTransformer;
import uk.nhs.digital.arc.transformer.publicationsystem.PubSysChangenoticeTransformer;
import uk.nhs.digital.arc.transformer.publicationsystem.PubSysInteractivetoolTransformer;
import uk.nhs.digital.arc.transformer.publicationsystem.PubSysLinkTransformer;
import uk.nhs.digital.arc.util.DateHelper;

import java.util.List;

/**
 * Given a Json document of type {@link Publication} we examine each field and use the values to either create
 * nodes that are added directly to our main content document or are added as subordinate nodes.
 *
 * In some instances, this node processing is done inline but in other cases we use Transformer classes
 * to do that work
 */
public class PublicationTransformer extends AbstractPageLevelTransformer {

    private Publication publication;

    /**
     * Since this class is a descendent of {@link AbstractPageLevelTransformer}, we only ever
     * expose the process method as a public method to deliver the final {@link ContentNode}
     * which wil have its own properties as well as subordinate nodes
     * @return is the now populated {@link ContentNode}
     */
    @Override
    public ContentNode process() {
        publication = (Publication) doctype;

        ContentNode contentNode = new ContentNode(publication.getTitleReq(), doctype.getDoctypeReq().toLowerCase());

        contentNode.setProperty(PUBLICATIONSYSTEM_TITLE_UC, publication.getTitleReq());
        contentNode.setProperty(PUBLICATIONSYSTEM_SUMMARY, publication.getSummaryReq());
        contentNode.setProperty(PUBLICATIONSYSTEM_NOMINALDATE, ContentPropertyType.DATE, DateHelper.massageDate(publication.getNominalDateReq()));
        contentNode.setProperty(PUBLICATIONSYSTEM_PUBLICALLYACCESSIBLE, publication.getPublicallyAccessibleReq());
        contentNode.setProperty(PUBLICATIONSYSTEM_COVERAGESTART, ContentPropertyType.DATE, DateHelper.massageDate(publication.getCoverageStart()));
        contentNode.setProperty(PUBLICATIONSYSTEM_COVERAGEEND, ContentPropertyType.DATE, DateHelper.massageDate(publication.getCoverageEnd()));

        processPageLevelFields(contentNode);
        processSections(contentNode);
        processAttachments(contentNode);
        return contentNode;
    }

    private void processPageLevelFields(ContentNode contentNode) {
        if (publication.getSeoSummary() != null) {
            ContentNode summaryNode = new ContentNode(PUBLICATIONSYSTEM_SEOSUMMARY, HIPPOSTD_HTML);
            summaryNode.setProperty(HIPPOSTD_CONTENT, publication.getSeoSummary());
            contentNode.addNode(summaryNode);
        }

        if (publication.getKeyFactsHead() != null) {
            ContentNode keyFactsHeadNode = new ContentNode(PUBLICATIONSYSTEM_KEYFACTSHEAD, HIPPOSTD_HTML);
            keyFactsHeadNode.setProperty(HIPPOSTD_CONTENT, publication.getKeyFactsHead());
            contentNode.addNode(keyFactsHeadNode);
        }

        if (publication.getKeyFactsTail() != null) {
            ContentNode keyFactsTailNode = new ContentNode(PUBLICATIONSYSTEM_KEYFACTSTAIL, HIPPOSTD_HTML);
            keyFactsTailNode.setProperty(HIPPOSTD_CONTENT, publication.getKeyFactsTail());
            contentNode.addNode(keyFactsTailNode);
        }

        if (publication.getPublicationSurvey() != null) {
            ContentNode surveyNode = new ContentNode(PUBLICATIONSYSTEM_SURVEY, PUBLICATIONSYSTEM_SURVEY);
            surveyNode.setProperty(PUBLICATIONSYSTEM_DATE,
                ContentPropertyType.DATE,
                DateHelper.massageDate(publication.getPublicationSurvey().getDateReq()));
            surveyNode.setProperty(PUBLICATIONSYSTEM_LINK, publication.getPublicationSurvey().getLinkReq());
            contentNode.addNode(surveyNode);
        }

        if (listExists(publication.getKeyFactsInfographics())) {
            for (PublicationBodyItem keyFactsInfographics : publication.getKeyFactsInfographics()) {
                InfographicTransformer transformer = new InfographicTransformer(session, keyFactsInfographics, docbase, storageManager);
                ContentNode infoGraphicsNode = transformer.process(AbstractTransformer.PUBLICATIONSYSTEM_KEYFACTINFOGRAPHICS);
                contentNode.addNode(infoGraphicsNode);
            }
        }

        if (listExists(publication.getResourceLinks())) {
            processLinkOfResourceOrExternalType(contentNode, publication.getResourceLinks(), AbstractTransformer.PUBLICATIONSYSTEM_RESOURCELINKS);
        }

        if (listExists(publication.getRelatedLinks())) {
            processLinkOfResourceOrExternalType(contentNode, publication.getRelatedLinks(), AbstractTransformer.PUBLICATIONSYSTEM_RELATEDLINKS);
        }

        if (listExists(publication.getChangeNotices())) {
            for (PublicationsystemChangeNotice changeNotice : publication.getChangeNotices()) {
                PubSysChangenoticeTransformer transformer = new PubSysChangenoticeTransformer(session, changeNotice);
                ContentNode noticeNode = transformer.process();
                contentNode.addNode(noticeNode);
            }
        }

        if (listExists(publication.getInteractiveTools())) {
            for (PublicationsystemInteractivetool interactivetool : publication.getInteractiveTools()) {
                PubSysInteractivetoolTransformer transformer = new PubSysInteractivetoolTransformer(session, interactivetool);
                ContentNode toolNode = transformer.process();
                contentNode.addNode(toolNode);
            }
        }
    }

    private void processLinkOfResourceOrExternalType(ContentNode sectionNode, List<PublicationsystemResourceOrExternalLink> links, String nodeType) {
        for (PublicationsystemResourceOrExternalLink resourceLink : links) {
            PubSysLinkTransformer transformer = new PubSysLinkTransformer(session, resourceLink, nodeType);
            ContentNode linkNode = transformer.process();
            sectionNode.addNode(linkNode);
        }
    }

    private void processSections(ContentNode contentNode) {
        if (listExists(publication.getSections())) {
            for (PublicationBodyItem section : publication.getSections()) {
                AbstractSectionTransformer sectionTransformer = getSectionTransformer(section);
                sectionTransformer.setStorageManager(storageManager);

                ContentNode sectionNode = sectionTransformer.process();
                contentNode.addNode(sectionNode);
            }
        }
    }

    private AbstractSectionTransformer getSectionTransformer(PublicationBodyItem section) {
        return
            InnerSectionTransformerFactory.getTransformerFromSectionType(section, session, docbase, storageManager);
    }

    private void processAttachments(ContentNode contentNode) {
        if (listExists(publication.getAttachments())) {
            for (PublicationsystemExternalattachment attachment : publication.getAttachments()) {
                String nodeTypeName = "Attachments-v3";
                String displayName = attachment.getDisplayName();
                String resource = attachment.getResource();

                populateAndCreateExternalAttachmentNode(contentNode,
                    nodeTypeName, displayName, resource, PUBLICATIONSYSTEM_ATTACHMENTRESOURCE);
            }
        }
    }
}
