package uk.nhs.digital.ps.modules.searchableFlagModule;

import static java.util.Collections.emptyMap;

import org.apache.sling.testing.mock.jcr.MockJcr;
import org.yaml.snakeyaml.Yaml;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JcrProvider {

    public Session getJcrFromFixture(InputStream fixture) {
        Repository repository = null;
        Session session = null;
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
                Node n = rootNode.addNode(node.path, node.primaryType);
                for (Map.Entry<String, Object> entry : node.properties.entrySet()) {
                    if (entry.getValue() instanceof Boolean) {
                        n.setProperty(entry.getKey(), (Boolean) entry.getValue());
                    } else {
                        n.setProperty(entry.getKey(), entry.getValue().toString());
                    }
                }
            }

            session.save();
        } catch (RepositoryException e) {
            throw new RuntimeException("Failed to set property on JCR node", e);
        }

        return session;
    }

    public static class NodeYaml {
        public String path;
        public String primaryType;
        public Map<String, Object> properties;

        public NodeYaml(String path, String primaryType, Map<String, Object> properties) {
            this.path = path;
            this.primaryType = primaryType;
            this.properties = properties;
        }
    }
}
