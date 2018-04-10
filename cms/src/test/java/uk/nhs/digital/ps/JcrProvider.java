package uk.nhs.digital.ps;

import static java.util.Collections.emptyMap;

import org.apache.sling.testing.mock.jcr.MockJcr;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class JcrProvider {

    public Session getJcrFromFixture(InputStream fixture) {
        Repository repository;
        Session session;
        try {
            repository = MockJcr.newRepository();
            session = repository.login();
            Node rootNode = session.getRootNode();

            Yaml parser = new Yaml();
            List<Map<String, Object>> nodes = (ArrayList<Map<String, Object>>) parser.load(fixture);

            for (Map object: nodes) {
                NodeYaml node = new NodeYaml(
                    (String) object.get("path"),
                    (String) object.get("primaryType"),
                    (Map<String, Object>) object.getOrDefault("properties", emptyMap())
                );
                Node secondaryNode = rootNode.addNode(node.path, node.primaryType);
                for (Map.Entry<String, Object> entry : node.properties.entrySet()) {
                    if (entry.getValue() instanceof Boolean) {
                        secondaryNode.setProperty(entry.getKey(), (Boolean) entry.getValue());
                    } else {
                        secondaryNode.setProperty(entry.getKey(), entry.getValue().toString());
                    }
                }
            }

            session.save();
        } catch (RepositoryException exception) {
            throw new RuntimeException("Failed to set property on JCR node", exception);
        }

        return session;
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
