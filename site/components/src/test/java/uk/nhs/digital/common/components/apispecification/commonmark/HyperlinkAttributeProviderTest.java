package uk.nhs.digital.common.components.apispecification.commonmark;

import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.openMocks;

import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Map;

public class HyperlinkAttributeProviderTest {
    // Bit of an overkill to use mock for simple map
    // but it enables nice, fluent syntax for verifications.
    @Mock
    private Map<String, String> attributes;

    private final HyperlinkAttributeProvider hyperlinkAttributeProvider = new HyperlinkAttributeProvider();

    @Before
    public void setUp() {
        openMocks(this);
    }

    @Test
    public void setsHyperlinkCssClassOnLinkElement() {

        // given
        final Node nodeLink = new Link();

        // when
        hyperlinkAttributeProvider.setAttributes(nodeLink, "tagName is ignored", attributes);

        // then
        then(attributes).should().put("class", "nhsd-a-link");
        then(attributes).shouldHaveNoMoreInteractions();
    }

    @Test
    public void ignoresElementOtherThanLink() {

        // given
        final Node nodeOtherThanLink = new TableBlock();

        // when
        hyperlinkAttributeProvider.setAttributes(nodeOtherThanLink, "tagName is ignored", attributes);

        // then
        then(attributes).shouldHaveNoInteractions();
    }
}
