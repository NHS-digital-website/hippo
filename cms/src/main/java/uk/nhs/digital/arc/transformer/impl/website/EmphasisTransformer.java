package uk.nhs.digital.arc.transformer.impl.website;

import org.apache.commons.lang3.StringUtils;
import org.onehippo.forge.content.pojo.model.ContentNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.arc.json.PublicationBodyItem;
import uk.nhs.digital.arc.json.website.WebsiteEmphasis;
import uk.nhs.digital.arc.storage.ArcStorageManager;
import uk.nhs.digital.arc.transformer.abs.AbstractSectionTransformer;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class EmphasisTransformer extends AbstractSectionTransformer {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmphasisTransformer.class);

    private final WebsiteEmphasis section;

    public EmphasisTransformer(Session session, PublicationBodyItem section, String docbase, ArcStorageManager storageManager) {
        super(session, docbase, storageManager);
        this.section = (WebsiteEmphasis)section;
    }

    @Override
    public ContentNode process() {
        //* Process section
        ContentNode sectionNode = new ContentNode("website:sections", "website:emphasisBox");

        sectionNode.setProperty("website:emphasisType", section.getEmphasisTypeReq());
        sectionNode.setProperty("website:heading", section.getHeading());

        setSingleNodeLevelProperty(sectionNode, "website:body", "hippostd:html", "hippostd:content", section.getBodyReq());

        processImage(sectionNode);

        return sectionNode;
    }

    private void processImage(ContentNode currentSectionNode) {
        if (!StringUtils.isEmpty(section.getImage())) {
            try {
                Node iconNode = session.getNode(section.getImage());
                ContentNode iconContent = setSingleNodeLevelProperty(currentSectionNode,
                    "website:image",
                    "hippogallerypicker:imagelink",
                    "hippo:docbase",
                    iconNode.getIdentifier());
                String[] categories = new String[]{"life", "cms"};
                iconContent.setProperty("hippo:values", categories);
                iconContent.setProperty("hippo:facets", new String[]{});
                iconContent.setProperty("hippo:modes", new String[]{"single"});
            } catch (RepositoryException e) {
                LOGGER.error("Error adding nodes for an image section", e);
            }
        }
    }
}
