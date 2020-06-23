package uk.nhs.digital.apispecs.commonmark;

import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;

import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.node.Code;
import org.commonmark.node.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Map;

public class CodeAttributeProviderTest {

    // Bit of an overkill to use mock for simple map
    // but it enables nice, fluent syntax for verifications.
    @Mock private Map<String, String> attributes;

    private CodeAttributeProvider codeAttributeProvider = new CodeAttributeProvider();

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void setsCodeInlineCssClassOnCodeElement() {

        // given
        final Node nodeCode = new Code();

        // when
        codeAttributeProvider.setAttributes(nodeCode, "tagName is ignored", attributes);

        // then
        then(attributes).should().put("class", "codeinline");
        then(attributes).shouldHaveNoMoreInteractions();
    }

    @Test
    public void ignoresElementOtherThanCode() {

        // given
        final Node nodeOtherThanCode = new TableBlock();

        // when
        codeAttributeProvider.setAttributes(nodeOtherThanCode, "tagName is ignored", attributes);

        // then
        then(attributes).shouldHaveZeroInteractions();
    }
}