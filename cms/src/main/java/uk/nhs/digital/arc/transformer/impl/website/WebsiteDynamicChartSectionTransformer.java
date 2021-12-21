package uk.nhs.digital.arc.transformer.impl.website;

import org.onehippo.forge.content.pojo.model.ContentNode;
import uk.nhs.digital.arc.json.PublicationBodyItem;
import uk.nhs.digital.arc.json.website.WebsiteDynamicChartSection;
import uk.nhs.digital.arc.storage.ArcStorageManager;
import uk.nhs.digital.arc.transformer.abs.AbstractSectionTransformer;

import javax.jcr.Session;

public class WebsiteDynamicChartSectionTransformer extends AbstractSectionTransformer {

    private final WebsiteDynamicChartSection chartSection;

    public WebsiteDynamicChartSectionTransformer(Session session, PublicationBodyItem section, String docbase, ArcStorageManager storageManager) {
        super(session, docbase, storageManager);
        chartSection = (WebsiteDynamicChartSection) section;
    }

    @Override
    public ContentNode process() {
        ContentNode sectionNode = new ContentNode(PUBLICATIONSYSTEM_BODYSECTIONS, PUBLICATIONSYSTEM_CHARTSECTION);

        sectionNode.setProperty(PUBLICATIONSYSTEM_TITLE, chartSection.getTitleReq());
        sectionNode.setProperty(PUBLICATIONSYSTEM_TYPE, chartSection.getType());
        sectionNode.setProperty(PUBLICATIONSYSTEM_YTITLE, chartSection.getYTitle());

        return sectionNode;
    }

}
