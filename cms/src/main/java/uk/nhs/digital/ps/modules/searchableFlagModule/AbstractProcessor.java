package uk.nhs.digital.ps.modules.searchableFlagModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

abstract class AbstractProcessor {

    private static final Logger log = LoggerFactory.getLogger(PublicationProcessor.class);

    protected static final String SEARCHABLE_FLAG = "common:searchable";
    private static final String PROPERTY_STATE = "hippostd:state";

    public AbstractProcessor() { }

    public abstract void processNode(Node node, List<String> validStatuses) throws RepositoryException;

    protected boolean hasValidStateAndType(Node node, List<String> validStates, String validType) {
        try {
            return hasValidState(node, validStates) && hasValidType(node, validType);
        } catch (RepositoryException ex) {
            log.error("RepositoryException during read operation", ex);
        }

        return false;
    }

    private boolean hasValidType(Node node, String validType) throws RepositoryException {
        return validType.equals(node.getPrimaryNodeType().getName());
    }

    private boolean hasValidState(Node node, List<String> validStates) throws RepositoryException {
        return validStates.contains(node.getProperty(PROPERTY_STATE).getString());
    }

    protected boolean hasValidDocumentType(Node node, String documentType) throws RepositoryException {
        return hasValidType(node.getNode(node.getName()), documentType);
    }

    protected Stream<Node> streamDocumentVariants(Node node) {
        return StreamSupport.stream(
            ((Iterable<Node>) () -> {
                    try {
                        return node.getNodes();
                    } catch (RepositoryException ex) {
                        log.error("RepositoryException during read operation", ex);
                    }
                    return null;
                }
            ).spliterator(),
            false
        );
    }
}
