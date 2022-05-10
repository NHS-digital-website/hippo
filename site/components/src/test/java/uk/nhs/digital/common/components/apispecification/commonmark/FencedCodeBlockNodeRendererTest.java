package uk.nhs.digital.common.components.apispecification.commonmark;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.openMocks;

import com.google.common.collect.ImmutableMap;
import org.commonmark.node.FencedCodeBlock;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import uk.nhs.digital.test.mockito.MockitoSessionTestBase;

import java.util.Collections;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class FencedCodeBlockNodeRendererTest extends MockitoSessionTestBase {

    private HtmlNodeRendererContext context;
    private FencedCodeBlockNodeRenderer renderer;

    @Mock
    private HtmlWriter html;

    @Before
    public void setUp() {
        context = Mockito.mock(HtmlNodeRendererContext.class);
        PowerMockito.when(context.getWriter()).thenReturn(html);
        renderer = new FencedCodeBlockNodeRenderer(context);
    }

    @Test
    public void ignoresNonCodeBlockElements() {
        Set<Class<? extends Node>> nodeTypes = renderer.getNodeTypes();
        assertThat(
            "FencedCodeBlockNodeRenderer only renders FencedCodeBlock nodes",
            nodeTypes, is(Collections.singleton(FencedCodeBlock.class))
        );
    }

    @Test
    public void rendersCodeBlock() {
        //Arrange
        final String literal = "<button class=\"nhsd-a-button\" type=\"button\">\n  <span class=\"nhsd-a-button__label\">Take primary action</span>\n</button>";
        final String language = "html";
        final String languageClass = String.format("language-%s", language);
        FencedCodeBlock node = new FencedCodeBlock();
        node.setInfo(language);
        node.setLiteral(literal);

        //Act
        renderer.render(node);

        //Assert
        InOrder inOrder = Mockito.inOrder(html);
        inOrder.verify(html).tag("article", Collections.singletonMap("class", "nhsd-o-code-viewer nhsd-t-body"));
        inOrder.verify(html).tag("div", ImmutableMap.of(
            "class", "nhsd-o-code-viewer__tab-content",
            "role", "tabpanel",
            "aria-hidden", "true"
        ));
        inOrder.verify(html).tag("p", ImmutableMap.of("class", "nhsd-t-heading-s nhsd-!t-margin-3","data-hide-tab-header", ""));
        inOrder.verify(html).text(language.toUpperCase());
        inOrder.verify(html).tag("/p");
        inOrder.verify(html).tag("div", Collections.singletonMap("class", "nhsd-o-code-viewer__code nhsd-o-code-viewer__code__slim"));
        inOrder.verify(html).tag("div", Collections.singletonMap("class", "nhsd-o-code-viewer__code-content nhsd-o-code-viewer__code-content__slim"));
        inOrder.verify(html).tag("pre", Collections.singletonMap("class", "line-numbers"));
        inOrder.verify(html).tag("code", Collections.singletonMap("class", languageClass));
        inOrder.verify(html).text(literal);
        inOrder.verify(html).tag("/code");
        inOrder.verify(html).tag("/pre");
        inOrder.verify(html, times(3)).tag("/div");
        inOrder.verify(html).tag("/article");
        inOrder.verifyNoMoreInteractions();
    }

}
