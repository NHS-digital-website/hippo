package uk.nhs.digital;

import static uk.nhs.digital.toolbox.exception.ExceptionUtils.wrapCheckedException;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;


public abstract class JcrRowUtils {

    @SuppressWarnings("unchecked")
    // it's guaranteed that RowIterator operates on instances of Row class
    public static Stream<Row> streamOf(final RowIterator rowIterator) {

        return StreamSupport.stream(
            Spliterators.<Row>spliteratorUnknownSize(rowIterator, Spliterator.ORDERED),
            false
        );
    }

    /**
     * This method is required as the {@link javax.jcr.query.Row} {@code getValue()}
     * implementation does not support multivalued properties and instead just
     * returns null.
     * @param row A row in the query result table.
     * @param nodeSelectorName The selector name used in the query to identify nodes
     * @param columnPropertyName The column property name in the table view of the
     *                           result set.
     * @return The multiple string property values at the indicated column and row.
     */
    public static List<String> getMultipleStringValueQuietly(final Row row, final String nodeSelectorName, final String columnPropertyName) {
        return Arrays
            .stream(wrapCheckedException(() -> row.getNode(nodeSelectorName).getProperty(columnPropertyName).getValues()))
            .map(value -> wrapCheckedException(value::getString))
            .collect(Collectors.toList());
    }
}