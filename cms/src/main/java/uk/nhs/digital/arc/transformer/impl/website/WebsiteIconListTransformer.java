package uk.nhs.digital.arc.transformer.impl.website;

import org.onehippo.forge.content.pojo.model.ContentNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.arc.json.PublicationBodyItem;
import uk.nhs.digital.arc.json.website.AbstractWebsiteItemlink;
import uk.nhs.digital.arc.json.website.WebsiteExternalLink;
import uk.nhs.digital.arc.json.website.WebsiteIconList;
import uk.nhs.digital.arc.json.website.WebsiteIconListItem;
import uk.nhs.digital.arc.json.website.WebsiteInternalLink;
import uk.nhs.digital.arc.storage.ArcStorageManager;
import uk.nhs.digital.arc.transformer.abs.AbstractSectionTransformer;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class WebsiteIconListTransformer extends AbstractSectionTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsiteIconListTransformer.class);

    private final WebsiteIconList section;

    public WebsiteIconListTransformer(Session session, PublicationBodyItem section, String docbase, ArcStorageManager storageManager) {
        super(session, docbase, storageManager);
        this.section = (WebsiteIconList)section;
    }

    @Override
    public ContentNode process() {
        ContentNode sectionNode = new ContentNode(PUBLICATIONSYSTEM_BODYSECTIONS, WEBSITE_ICONLIST);
        sectionNode.setProperty(WEBSITE_HEADINGLEVEL, section.getHeadinglevelReq());
        sectionNode.setProperty(WEBSITE_TITLE, section.getTitle());

        addIntroductionNode(sectionNode);

        try {
            addIconListItems(sectionNode);
        } catch (RepositoryException e) {
            LOGGER.error("Unable to add icon list details to the repository", e);
        }

        return sectionNode;
    }

    private void addIconListItems(ContentNode sectionNode) throws RepositoryException {
        if (listExists(section.getIconlistitems())) {
            for (WebsiteIconListItem item : section.getIconlistitems()) {
                ContentNode iconListItemsNode = new ContentNode(WEBSITE_ICONLISTITEMS, WEBSITE_ICONLISTITEM);
                iconListItemsNode.setProperty(WEBSITE_HEADING, item.getHeading());

                ContentNode descriptionNode = new ContentNode(WEBSITE_DESCRIPTION, HIPPOSTD_HTML);
                descriptionNode.setProperty(HIPPOSTD_CONTENT, item.getDescriptionReq());
                iconListItemsNode.addNode(descriptionNode);

                Node imageDocbase = session.getNode(item.getImageReq());

                ContentNode imageNode = setSingleNodeLevelProperty(iconListItemsNode, WEBSITE_IMAGE,
                    HIPPOGALLERYPICKER_IMAGELINK,
                    HIPPO_DOCBASE,
                    imageDocbase.getIdentifier());
                imageNode.setProperty(HIPPO_FACETS, new String[]{});
                imageNode.setProperty(HIPPO_MODES, new String[]{});
                imageNode.setProperty(HIPPO_VALUES, new String[]{});

                if (listExists(item.getItemlink())) {
                    ContentNode websiteItemLink = null;

                    if (isInternal(item.getItemlink())) {
                        WebsiteInternalLink intLink = (WebsiteInternalLink)item.getItemlink().get(0);

                        websiteItemLink = new ContentNode(WEBSITE_ITEMLINK, WEBSITE_INTERNALLINK);

                        Node linkRefNode = session.getNode(intLink.getLinkReq());
                        setSingleNodeLevelProperty(websiteItemLink, WEBSITE_LINK,
                            HIPPO_MIRROR,
                            HIPPO_DOCBASE,
                            linkRefNode.getIdentifier());
                    } else {
                        WebsiteExternalLink extLink = (WebsiteExternalLink)item.getItemlink().get(0);

                        websiteItemLink = new ContentNode(WEBSITE_ITEMLINK, WEBSITE_EXTERNALLINK);

                        websiteItemLink.setProperty(WEBSITE_LINK, extLink.getLinkReq());
                        websiteItemLink.setProperty(WEBSITE_SHORTSUMMARY, extLink.getShortsummaryReq());
                        websiteItemLink.setProperty(WEBSITE_TITLE, extLink.getTitleReq());
                    }

                    iconListItemsNode.addNode(websiteItemLink);
                }
                sectionNode.addNode(iconListItemsNode);
            }
        }
    }

    private boolean isInternal(List<AbstractWebsiteItemlink> itemlink) {
        if (listExists(itemlink)) {
            return itemlink.get(0) instanceof WebsiteInternalLink;
        }

        return false;
    }


    private void addIntroductionNode(ContentNode sectionNode) {
        if (null != section.getIntroduction()) {
            ContentNode introNode = new ContentNode(WEBSITE_INTRODUCTION, HIPPOSTD_HTML);
            introNode.setProperty(HIPPOSTD_CONTENT, section.getIntroduction());
            sectionNode.addNode(introNode);
        }
    }
}
