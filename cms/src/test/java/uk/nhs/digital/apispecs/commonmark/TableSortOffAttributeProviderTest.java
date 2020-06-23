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

public class TableSortOffAttributeProviderTest {

    // Bit of an overkill to use mock for simple map
    // but it enables nice, fluent syntax for verifications.
    @Mock private Map<String, String> attributes;

    private TableSortOffAttributeProvider tableSortOffAttributeProvider = new TableSortOffAttributeProvider();

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void setsDisableSortAttributeOnTableElement() {

        // given
        final Node nodeTable = new TableBlock();

        // when
        tableSortOffAttributeProvider.setAttributes(nodeTable, "tagName is ignored", attributes);

        // then
        then(attributes).should().put("data-disablesort", "true");
        then(attributes).shouldHaveNoMoreInteractions();
    }

    @Test
    public void ignoresElementOtherThanTable() {

        // given
        final Node nodeOtherThanTable = new Code();

        // when
        tableSortOffAttributeProvider.setAttributes(nodeOtherThanTable, "tagName is ignored", attributes);

        // then
        then(attributes).shouldHaveZeroInteractions();
    }
}