package uk.nhs.digital.common.components.apispecification.commonmark;

import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.openMocks;

import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.node.Node;
import org.commonmark.node.ThematicBreak;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Map;


public class ThematicBreakAttributeProviderTest {

    @Mock
    private Map<String, String> attributes;

    private ThematicBreakAttributeProvider thematicBreakAttributeProvider = new ThematicBreakAttributeProvider();

    @Before
    public void setUp() {
        openMocks(this);
    }

    @Test
    public void setsThematicBreakInlineCssClassOnCodeElement() {

        // given
        final Node thematicBreakNode = new ThematicBreak();

        // when
        thematicBreakAttributeProvider.setAttributes(thematicBreakNode, "tagName is ignored", attributes);

        // then
        then(attributes).should().put("class", "nhsd-a-horizontal-rule");
        then(attributes).shouldHaveNoMoreInteractions();
    }

    @Test
    public void ignoresElementOtherThanThematicBreak() {

        // given
        final Node nodeOtherThanThematicBreak = new TableBlock();

        // when
        thematicBreakAttributeProvider.setAttributes(nodeOtherThanThematicBreak, "tagName is ignored", attributes);

        // then
        then(attributes).shouldHaveNoInteractions();
    }
}
