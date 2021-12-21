package uk.nhs.digital.arc.transformer.impl.website;

import org.onehippo.forge.content.pojo.model.ContentNode;
import uk.nhs.digital.arc.json.PublicationBodyItem;
import uk.nhs.digital.arc.json.website.WebsiteSection;
import uk.nhs.digital.arc.storage.ArcStorageManager;
import uk.nhs.digital.arc.transformer.abs.AbstractSectionTransformer;

import javax.jcr.Session;

public class PublicationWebsiteSectionTransformer extends AbstractSectionTransformer {

    private final WebsiteSection section;
    private String contentNodeName;

    public PublicationWebsiteSectionTransformer(Session session, PublicationBodyItem section, String docbase, ArcStorageManager storageManager) {
        super(session, docbase, storageManager);
        this.section = (WebsiteSection)section;
        this.setContentNodeName(PUBLICATIONSYSTEM_BODYSECTIONS);
    }

    /**
     * This transformer does its own work but is also extended for body
     * items (notably {@link BodyItemSectionTransformer}), so we need to
     * set the name of the containing node as a property per case
     * @param contentNodeName is the name of that parent node
     */
    protected void setContentNodeName(String contentNodeName) {
        this.contentNodeName = contentNodeName;
    }

    @Override
    public ContentNode process() {
        ContentNode sectionNode = new ContentNode(contentNodeName, WEBSITE_SECTION);

        sectionNode.setProperty(WEBSITE_HEADINGLEVEL, section.getHeadingLevel());
        sectionNode.setProperty(WEBSITE_TITLE, section.getTitle());

        if (section.getHtml() != null) {
            ContentNode htmlNode = new ContentNode(WEBSITE_HTML, HIPPOSTD_HTML);
            htmlNode.setProperty(HIPPOSTD_CONTENT, section.getHtml());
            sectionNode.addNode(htmlNode);
        }

        return sectionNode;
    }
}
