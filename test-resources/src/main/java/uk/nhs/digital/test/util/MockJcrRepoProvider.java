package uk.nhs.digital.test.util;

import static java.util.Collections.emptyMap;

import org.apache.jackrabbit.value.DateValue;
import org.apache.sling.testing.mock.jcr.MockJcr;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.jcr.*;

public class MockJcrRepoProvider {

    public static Session repoFromYaml(final String fileClassPath) {

        Repository repository;
        Session session;
        try {
            repository = MockJcr.newRepository();
            session = repository.login();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load JCR content from fixture file " + fileClassPath, e);
        }

        initJcrRepoFromYaml(session, fileClassPath);

        return session;
    }

    public static void initJcrRepoFromYaml(final Session session, final String fileClassPath) {

        try {
            Node rootNode = session.getRootNode();

            List<Map<String, Object>> nodes = parseYaml(fileClassPath);

            for (Map object : nodes) {
                NodeYaml node = new NodeYaml(
                    (String) object.get("path"),
                    (String) object.get("primaryType"),
                    (Map<String, Object>) object.getOrDefault("properties", emptyMap())
                );
                Node secondaryNode = rootNode.addNode(node.path, node.primaryType);
                for (Map.Entry<String, Object> entry : node.properties.entrySet()) {
                    if (entry.getValue() instanceof Boolean) {
                        addBooleanProperty(secondaryNode, entry);
                    } else if (entry.getValue() instanceof Date) {
                        addDateProperty(secondaryNode, entry);
                    } else if (entry.getValue() instanceof List) {
                        addMultiValueProperty(secondaryNode, entry);
                    } else {
                        addStringProperty(secondaryNode, entry);
                    }
                }
            }

            session.save();
        } catch (final Exception exception) {
            throw new RuntimeException("Failed to load JCR content from fixture file " + fileClassPath, exception);
        }
    }

    private static void addBooleanProperty(final Node node, final Map.Entry<String, Object> entry) throws RepositoryException {
        node.setProperty(entry.getKey(), (Boolean)entry.getValue());
    }

    private static void addStringProperty(final Node node, final Map.Entry<String, Object> entry) throws RepositoryException {
        node.setProperty(entry.getKey(), entry.getValue().toString());
    }

    private static void addDateProperty(final Node node, final Map.Entry<String, Object> entry) throws RepositoryException {
        node.setProperty(entry.getKey(), toCalendar((Date) entry.getValue()));
    }

    private static void addMultiValueProperty(final Node node, final Map.Entry<String, Object> entry) throws RepositoryException {

        final List<?> values = (List<?>) entry.getValue();

        if (values.isEmpty()) {
            node.setProperty(entry.getKey(), new Value[0]);
            return;
        }

        if (values.get(0) instanceof Date) {
            addMultiValueDateProperty(node, entry.getKey(), (List<Date>)values);
        } else {
            addMultiValueStringProperty(node, entry.getKey(), (List<String>)values);
        }
    }

    private static void addMultiValueStringProperty(final Node node, final String propertyName, final List<String> values) throws RepositoryException {
        node.setProperty(propertyName, values.toArray(new String[0]));
    }

    private static void addMultiValueDateProperty(final Node node, final String propertyName, final List<Date> values) throws RepositoryException {
        final DateValue[] dates = values.stream()
            .map(date -> new Calendar.Builder().setInstant(date).build())
            .map(DateValue::new)
            .toArray(DateValue[]::new);

        node.setProperty(propertyName, dates);
    }

    private static Calendar toCalendar(final Date date) {
        return new Calendar.Builder().setInstant(date).build();
    }

    private static List<Map<String, Object>> parseYaml(final String fileClassPath) {

        final Yaml parser = new Yaml();
        try (InputStream fixtureYamlFileInputStream =
                 MockJcrRepoProvider.class.getResourceAsStream(fileClassPath)) {

            if (fixtureYamlFileInputStream == null) {
                throw new RuntimeException("No file was available at " + fileClassPath);
            }

            return parser.load(fixtureYamlFileInputStream);

        } catch (final Exception e) {
            throw new RuntimeException("Failed to parse the fixture file", e);
        }
    }

    private static class NodeYaml {
        private String path;
        private String primaryType;
        private Map<String, Object> properties;

        private NodeYaml(String path, String primaryType, Map<String, Object> properties) {
            this.path = path;
            this.primaryType = primaryType;
            this.properties = properties;
        }
    }
}
