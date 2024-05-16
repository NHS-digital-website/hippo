package uk.nhs.digital;

import static uk.nhs.digital.toolbox.exception.ExceptionUtils.wrapCheckedException;

import org.apache.commons.lang3.Validate;
import org.apache.jackrabbit.value.DateValue;
import org.hippoecm.repository.util.JcrUtils;

import java.sql.Date;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import javax.jcr.Node;
import javax.jcr.Session;

public abstract class JcrNodeUtils {

    public static Session getSessionQuietly(final Node node) {
        return wrapCheckedException(node::getSession);
    }

    public static Optional<Boolean> getBooleanPropertyQuietly(final Node node, final String propertyName) {
        return Optional.ofNullable(
            wrapCheckedException(() -> JcrUtils.getBooleanProperty(node, propertyName, null))
        );
    }

    public static Optional<String> getStringPropertyQuietly(final Node node, final String propertyName) {
        return Optional.ofNullable(
            wrapCheckedException(() -> JcrUtils.getStringProperty(node, propertyName, null))
        );
    }

    public static void setStringPropertyQuietly(final Node node, final String propertyName, final String value) {
        wrapCheckedException(() -> node.setProperty(propertyName, value));
    }

    public static List<String> getMultipleStringPropertyQuietly(final Node node, final String propertyName) {
        return Arrays.stream(wrapCheckedException(() -> JcrUtils.getMultipleStringProperty(node, propertyName, new String[0])))
            .collect(Collectors.toList());
    }

    public static void setMultipleStringPropertyQuietly(final Node node, final String propertyName, Collection<String> values) {

        final String[] valuesArray = values == null
            ? new String[0]
            : values.toArray(values.toArray(new String[0]));

        setMultipleStringPropertyQuietly(node, propertyName, valuesArray);
    }

    public static void setMultipleStringPropertyQuietly(final Node node, final String propertyName, final String[] values) {
        wrapCheckedException(() -> node.getProperty(propertyName).setValue(values));
    }

    public static Optional<Instant> getInstantPropertyQuietly(final Node node, final String propertyName) {
        return wrapCheckedException(() ->
            Optional.ofNullable(JcrUtils.getDateProperty(node, propertyName, null)).map(Calendar::toInstant)
        );
    }

    public static void setInstantPropertyQuietly(final Node node, final String propertyName, final Instant instant) {
        wrapCheckedException(() -> node.setProperty(propertyName, calendarFrom(instant)));
    }

    public static List<Instant> getMultipleInstantPropertyQuietly(final Node node, final String propertyName) {
        return Arrays.stream(wrapCheckedException(() -> node.getProperty(propertyName).getValues()))
            .map(value -> wrapCheckedException(value::getDate))
            .map(Calendar::toInstant)
            .collect(Collectors.toList());
    }

    public static void setMultipleInstantPropertyQuietly(final Node node, final String propertyName, final Collection<Instant> values) {

        final Instant[] instants = values == null
            ? new Instant[0]
            : values.toArray(new Instant[0]);

        setMultipleInstantPropertyQuietly(node, propertyName, instants);
    }

    public static void setMultipleInstantPropertyQuietly(final Node node, final String propertyName, final Instant[] values) {

        final DateValue[] dateValues = Arrays.stream(values)
            .map(instant -> new Calendar.Builder().setInstant(Date.from(instant)).build())
            .map(DateValue::new)
            .toArray(DateValue[]::new);

        wrapCheckedException(() -> node.getProperty(propertyName).setValue(dateValues));
    }

    public static String getNodeIdQuietly(final Node node) {
        return wrapCheckedException(node::getIdentifier);
    }

    public static void validateIsOfTypeHandle(final Node documentHandleCandidateNode) {
        Validate.isTrue(
            wrapCheckedException(() -> documentHandleCandidateNode.isNodeType("hippo:handle")),
            "Node's 'jcr:primaryType' property has to be of type 'hippo:handle'"
        );
    }

    private static Calendar calendarFrom(final Instant instant) {
        return new Calendar.Builder().setInstant(Date.from(instant)).build();
    }
}
