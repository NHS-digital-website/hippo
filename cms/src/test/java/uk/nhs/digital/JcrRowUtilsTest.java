package uk.nhs.digital;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.apache.jackrabbit.commons.iterator.RowIteratorAdapter;
import org.apache.jackrabbit.value.StringValue;
import org.junit.Test;
import uk.nhs.digital.toolbox.exception.ExceptionUtils;

import java.util.List;
import java.util.stream.Stream;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;

public class JcrRowUtilsTest {

    private static StringValue[] fromStrings(final List<String> values) {
        return values.stream().map(StringValue::new).toArray(StringValue[]::new);
    }

    @Test
    public void streamOf_returnsNodesFromNodeIteratorAsStream() {
        // given
        final Row rowA = mock(Row.class);
        final Row rowB = mock(Row.class);
        final Row rowC = mock(Row.class);

        final RowIterator rowIterator = new RowIteratorAdapter(Stream.of(rowA, rowB, rowC).iterator());

        // when
        final Stream<Row> actualRowStream = JcrRowUtils.streamOf(rowIterator);

        // then
        assertThat(
            "Returned stream contains rows from the iterator, exactly.",
            actualRowStream.collect(toList()),
            containsInAnyOrder(rowA, rowB, rowC)
        );
    }

    @Test
    public void getMultipleStringValueQuietly_returnsValueOfGivenPropertyOnGivenRow() throws RepositoryException {
        // given
        final String expectedSelectorName = "expectedSelectorName";
        final String expectedColumnPropertyName = "expectedColumnPropertyName";
        final List<String> expectedPropertyValues = asList("value-a", "value-b");

        final StringValue[] storedPropertyValues = fromStrings(expectedPropertyValues);

        final Property expectedProperty = mock(Property.class);
        given(expectedProperty.getValues()).willReturn(storedPropertyValues);

        final Node node = mock(Node.class);
        given(node.getProperty(expectedColumnPropertyName)).willReturn(expectedProperty);

        final Row row = mock(Row.class);
        given(row.getNode(expectedSelectorName)).willReturn(node);

        // when
        final List<String> actualPropertyValues =
            JcrRowUtils.getMultipleStringValueQuietly(row, expectedSelectorName, expectedColumnPropertyName);

        // then
        assertThat(
            "Value returned was read from the given row",
            actualPropertyValues,
            is(expectedPropertyValues)
        );
    }

    @Test(expected = ExceptionUtils.UncheckedWrappingException.class)
    public void getMultipleStringValueQuietly_throwsException_onFailure() throws RepositoryException {
        // given
        final RepositoryException repositoryException = new RepositoryException();

        final Row row = mock(Row.class);
        given(row.getNode(any())).willThrow(repositoryException);

        // when
        JcrRowUtils.getMultipleStringValueQuietly(row, "columnSelectorName", "columnPropertyName");

        // then
        // expected Exception set in @Test annotation is thrown
    }
}
