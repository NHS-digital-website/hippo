package uk.nhs.digital.arc.transformer.impl.website;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.onehippo.forge.content.pojo.model.ContentNode;
import uk.nhs.digital.arc.json.PublicationBodyItem;
import uk.nhs.digital.arc.json.website.AbstractWebsiteItemlink;
import uk.nhs.digital.arc.json.website.WebsiteExternalLink;
import uk.nhs.digital.arc.json.website.WebsiteIconList;
import uk.nhs.digital.arc.json.website.WebsiteIconListItem;
import uk.nhs.digital.arc.json.website.WebsiteInternalLink;
import uk.nhs.digital.arc.storage.ArcStorageManager;
import uk.nhs.digital.arc.transformer.abs.AbstractSectionTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class WebsiteIconListTransformerTest {

    public static final String ITEM_IMAGE_LOCATION = "ITEM_IMAGE_LOCATION";
    public static final String INTERNAL_LINK_LOCATION = "INT_LINK";
    public static final String EXTERNAL_LINK_LOCATION = "EXT_LINK";
    public static final String ITEM_HEADING = "ITEM HEADING";
    public static final String ITEM_DESCRIPTION = "ITEM DESCRIPTION";
    public static final String TITLE = "TITLE";
    public static final String MAIN = "MAIN";
    public static final String INTRO = "INTRO";
    public static final String SUMMARY = "SUMMARY";
    public static final String DOCBASE = "docbase";

    @Mock
    Session mockSession;

    @Mock
    Node mockImageNode;

    @Mock
    Node mockInternalLinkJcrNode;

    @Mock
    ArcStorageManager mockStorageManager;

    @Before
    public void setUp() {
        openMocks(this);
    }

    @Test
    public void testInternalLinkCreatesApporopriateNodes() throws RepositoryException {
        // given
        WebsiteIconListTransformer transformer = getWebsiteIconlistTransformer(true);

        // when
        ContentNode response = transformer.process();

        // then - main node proprties
        assertEquals(TITLE, response.getProperty(AbstractSectionTransformer.WEBSITE_TITLE).getValue());
        assertEquals(MAIN, response.getProperty(AbstractSectionTransformer.WEBSITE_HEADINGLEVEL).getValue());

        // then - introduction node properties
        ContentNode introNode = response.getNode(AbstractSectionTransformer.WEBSITE_INTRODUCTION);
        assertNotNull(introNode);
        assertEquals(INTRO, introNode.getProperty(AbstractSectionTransformer.HIPPOSTD_CONTENT).getValue());

        // then  - item nodes - one per icon list item
        assertThereAreExactly(response, AbstractSectionTransformer.WEBSITE_ICONLISTITEMS, 1);
        ContentNode iconListItemsNode = assertNodeNotNull(response, AbstractSectionTransformer.WEBSITE_ICONLISTITEMS);

        // then - one description per node
        ContentNode descrNode = assertNodeNotNull(iconListItemsNode, AbstractSectionTransformer.WEBSITE_DESCRIPTION);
        assertEquals(ITEM_DESCRIPTION, descrNode.getProperty(AbstractSectionTransformer.HIPPOSTD_CONTENT).getValue());

        // then - one image per node
        assertThereAreExactly(iconListItemsNode, AbstractSectionTransformer.WEBSITE_IMAGE, 1);
        ContentNode imageNode = assertNodeNotNull(iconListItemsNode, AbstractSectionTransformer.WEBSITE_IMAGE);
        assertEquals("abcdef", imageNode.getProperty(AbstractSectionTransformer.HIPPO_DOCBASE).getValue());

        assertForInternalLinks(iconListItemsNode);
    }

    @Test
    public void testExternalLinkCreatesApporopriateNodes() throws RepositoryException {
        // given
        WebsiteIconListTransformer transformer = getWebsiteIconlistTransformer(false);

        // when
        ContentNode response = transformer.process();

        // then - main node proprties
        assertEquals(TITLE, response.getProperty(AbstractSectionTransformer.WEBSITE_TITLE).getValue());
        assertEquals(MAIN, response.getProperty(AbstractSectionTransformer.WEBSITE_HEADINGLEVEL).getValue());

        // then - introduction node properties
        ContentNode introNode = response.getNode(AbstractSectionTransformer.WEBSITE_INTRODUCTION);
        assertNotNull(introNode);

        // then  - item nodes - one per icon list item
        assertThereAreExactly(response, AbstractSectionTransformer.WEBSITE_ICONLISTITEMS, 1);
        ContentNode iconListItemsNode = assertNodeNotNull(response, AbstractSectionTransformer.WEBSITE_ICONLISTITEMS);

        // then - one description per node
        ContentNode descrNode = assertNodeNotNull(iconListItemsNode, AbstractSectionTransformer.WEBSITE_DESCRIPTION);
        assertEquals(ITEM_DESCRIPTION, descrNode.getProperty(AbstractSectionTransformer.HIPPOSTD_CONTENT).getValue());

        // then - one image per node
        assertThereAreExactly(iconListItemsNode, AbstractSectionTransformer.WEBSITE_IMAGE, 1);
        ContentNode imageNode = assertNodeNotNull(iconListItemsNode, AbstractSectionTransformer.WEBSITE_IMAGE);
        assertEquals("abcdef", imageNode.getProperty(AbstractSectionTransformer.HIPPO_DOCBASE).getValue());

        assertForExternalLinks(iconListItemsNode);
    }

    private void assertForInternalLinks(ContentNode iconListItemsNode) {
        // then one itemlink per node (or type internal) - which itself contains one link
        assertThereAreExactly(iconListItemsNode, AbstractSectionTransformer.WEBSITE_ITEMLINK, 1);
        ContentNode itemLinkNode = assertNodeNotNull(iconListItemsNode, AbstractSectionTransformer.WEBSITE_ITEMLINK);
        assertEquals(AbstractSectionTransformer.WEBSITE_INTERNALLINK, itemLinkNode.getPrimaryType());

        // then - there should be just one link beneath the internal link node
        assertThereAreExactly(itemLinkNode, AbstractSectionTransformer.WEBSITE_LINK, 1);
        ContentNode linkNode = assertNodeNotNull(itemLinkNode, AbstractSectionTransformer.WEBSITE_LINK);
        assertEquals(AbstractSectionTransformer.WEBSITE_INTERNALLINK, itemLinkNode.getPrimaryType());
    }

    private void assertForExternalLinks(ContentNode iconListItemsNode) {
        // then one itemlink per node (for type external) - which itself contains no child nodes
        assertThereAreExactly(iconListItemsNode, AbstractSectionTransformer.WEBSITE_ITEMLINK, 1);
        ContentNode itemLinkNode = assertNodeNotNull(iconListItemsNode, AbstractSectionTransformer.WEBSITE_ITEMLINK);
        assertEquals(AbstractSectionTransformer.WEBSITE_EXTERNALLINK, itemLinkNode.getPrimaryType());

        assertEquals(EXTERNAL_LINK_LOCATION, itemLinkNode.getProperty(AbstractSectionTransformer.WEBSITE_LINK).getValue());
        assertEquals(SUMMARY, itemLinkNode.getProperty(AbstractSectionTransformer.WEBSITE_SHORTSUMMARY).getValue());
        assertEquals(TITLE, itemLinkNode.getProperty(AbstractSectionTransformer.WEBSITE_TITLE).getValue());

        // then - there should be just one link beneath the internal link node
        assertThereAreExactly(itemLinkNode, AbstractSectionTransformer.WEBSITE_LINK, 0);
    }

    private void assertThereAreExactly(ContentNode parentNode, String nodeName, int expectedCount) {
        List<ContentNode> nodes = parentNode.getNodes();
        assertEquals(expectedCount, nodes.stream().filter(n -> n.getName().equals(nodeName)).count());
    }

    private ContentNode assertNodeNotNull(ContentNode parentNode, String nodeName) {
        ContentNode responseNode = parentNode.getNode(nodeName);
        assertNotNull(responseNode);
        return responseNode;
    }

    private WebsiteIconListTransformer getWebsiteIconlistTransformer(boolean internal) throws RepositoryException {
        when(mockSession.getNode(ITEM_IMAGE_LOCATION)).thenReturn(mockImageNode);
        when(mockSession.getNode(INTERNAL_LINK_LOCATION)).thenReturn(mockInternalLinkJcrNode);
        when(mockImageNode.getIdentifier()).thenReturn("abcdef");
        when(mockInternalLinkJcrNode.getIdentifier()).thenReturn("fedcba");

        WebsiteIconListTransformer transformer = new WebsiteIconListTransformer(mockSession, createIconList(internal), DOCBASE, mockStorageManager);
        return transformer;
    }

    private PublicationBodyItem createIconList(boolean internal) {
        AbstractWebsiteItemlink link = null;

        if (internal) {
            link = new WebsiteInternalLink(INTERNAL_LINK_LOCATION);
        } else {
            link = new WebsiteExternalLink(TITLE, SUMMARY, EXTERNAL_LINK_LOCATION);
        }

        List<AbstractWebsiteItemlink> internalLinks = new ArrayList<>();
        internalLinks.add(link);

        WebsiteIconListItem iconListItem = new WebsiteIconListItem();
        iconListItem.setHeading(ITEM_HEADING);
        iconListItem.setDescriptionReq(ITEM_DESCRIPTION);
        iconListItem.setImageReq(ITEM_IMAGE_LOCATION);
        iconListItem.setItemlink(internalLinks);
        List<WebsiteIconListItem> iconListItems = new ArrayList<>();
        iconListItems.add(iconListItem);

        WebsiteIconList iconList = new WebsiteIconList();
        iconList.setTitle(TITLE);
        iconList.setHeadinglevelReq(MAIN);
        iconList.setIntroduction(INTRO);

        iconList.setIconlistitems(iconListItems);

        return iconList;
    }
}