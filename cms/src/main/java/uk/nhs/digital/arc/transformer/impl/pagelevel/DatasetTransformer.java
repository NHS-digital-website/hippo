package uk.nhs.digital.arc.transformer.impl.pagelevel;

import org.onehippo.forge.content.pojo.model.ContentNode;
import org.onehippo.forge.content.pojo.model.ContentPropertyType;
import uk.nhs.digital.arc.json.Dataset;
import uk.nhs.digital.arc.json.publicationsystem.PublicationsystemResourceOrExternalLink;
import uk.nhs.digital.arc.transformer.abs.AbstractPageLevelTransformer;
import uk.nhs.digital.arc.util.DateHelper;

public class DatasetTransformer extends AbstractPageLevelTransformer {

    private Dataset dataset;

    @Override
    public ContentNode process() {
        dataset = (Dataset) doctype;

        ContentNode cn = new ContentNode(dataset.getTitleReq(), doctype.getDoctypeReq().toLowerCase());

        cn.setProperty(PUBLICATIONSYSTEM_TITLE_UC, dataset.getTitleReq());
        cn.setProperty(PUBLICATIONSYSTEM_SUMMARY, dataset.getSummaryReq());
        cn.setProperty(PUBLICATIONSYSTEM_NOMINALDATE, ContentPropertyType.DATE, DateHelper.massageDate(dataset.getNominalDateReq()));

        setMultipleProp(cn, PUBLICATIONSYSTEM_GEOGRAPHICCOVERAGE, dataset.getGeographicCoverage());
        setMultipleProp(cn, PUBLICATIONSYSTEM_GRANULARITY, dataset.getGranularity());
        cn.setProperty(PUBLICATIONSYSTEM_NEXTPUBLICATIONDATE, ContentPropertyType.DATE, DateHelper.massageDate(dataset.getNextPublicationDate()));
        cn.setProperty(PUBLICATIONSYSTEM_COVERAGESTART, ContentPropertyType.DATE, DateHelper.massageDate(dataset.getCoverageStart()));
        cn.setProperty(PUBLICATIONSYSTEM_COVERAGEEND, ContentPropertyType.DATE, DateHelper.massageDate(dataset.getCoverageEnd()));

        processAttachments(cn);
        processLinks(cn);
        return cn;
    }

    private void processAttachments(ContentNode cn) {
        if (listExists(dataset.getRelatedlinks())) {
            for (PublicationsystemResourceOrExternalLink link: dataset.getRelatedlinks()) {
                String nodeTypeName = "Files-v3";
                String displayName = link.getLinkTextReq();
                String resource = link.getLinkUrlReq();

                populateAndCreateExternalAttachmentNode(cn, nodeTypeName, displayName, resource, PUBLICATIONSYSTEM_ATTACHMENTRESOURCE);
            }
        }
    }

    private void processLinks(ContentNode cn) {
        if (listExists(dataset.getResourceLinks())) {
            for (PublicationsystemResourceOrExternalLink link : dataset.getResourceLinks()) {
                ContentNode linkNode = new ContentNode(PUBLICATIONSYSTEM_RESOURCELINKS, PUBLICATIONSYSTEM_RELATEDLINK);
                linkNode.setProperty(PUBLICATIONSYSTEM_LINKTEXT, link.getLinkTextReq());
                linkNode.setProperty(PUBLICATIONSYSTEM_LINKURL, link.getLinkUrlReq());

                cn.addNode(linkNode);
            }
        }
    }
}