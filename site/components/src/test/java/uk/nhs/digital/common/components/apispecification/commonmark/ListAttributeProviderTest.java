package uk.nhs.digital.common.components.apispecification.commonmark;

import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.openMocks;

import org.commonmark.node.BulletList;
import org.commonmark.node.Code;
import org.commonmark.node.Node;
import org.commonmark.node.OrderedList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Map;

public class ListAttributeProviderTest {

    // Bit of an overkill to use mock for simple map
    // but it enables nice, fluent syntax for verifications.
    @Mock
    private Map<String, String> attributes;

    private ListAttributeProvider listAttributeProvider = new ListAttributeProvider();

    @Before
    public void setUp() {
        openMocks(this);
    }

    @Test
    public void setsCssClassOnBulletListElement() {

        // given
        final Node nodeBulletList = new BulletList();

        // when
        listAttributeProvider.setAttributes(nodeBulletList, "tagName is ignored", attributes);

        // then
        then(attributes).should().put("class", "nhsd-t-list nhsd-t-list--bullet");
        then(attributes).shouldHaveNoMoreInteractions();
    }

    @Test
    public void setsCssClassOnOrderedListElement() {

        // given
        final Node nodeOrderedList = new OrderedList();

        // when
        listAttributeProvider.setAttributes(nodeOrderedList, "tagName is ignored", attributes);

        // then
        then(attributes).should().put("class", "nhsd-t-list nhsd-t-list--number");
        then(attributes).shouldHaveNoMoreInteractions();
    }

    @Test
    public void ignoresElementOtherThanList() {

        // given
        final Node nodeOtherThanTable = new Code();

        // when
        listAttributeProvider.setAttributes(nodeOtherThanTable, "tagName is ignored", attributes);

        // then
        then(attributes).shouldHaveNoMoreInteractions();
    }
}
