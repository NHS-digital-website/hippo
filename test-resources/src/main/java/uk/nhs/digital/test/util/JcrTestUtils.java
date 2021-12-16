package uk.nhs.digital.test.util;

import static uk.nhs.digital.test.util.ExceptionTestUtils.*;

import java.time.Instant;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class JcrTestUtils {

    public static Node getDocumentVariantNode(
        final Node documentHandleNode,
        final JcrTestUtils.BloomReachJcrDocumentVariantType bloomReachJcrDocumentVariantType
    ) {
        return findDocumentVariantNode(documentHandleNode, bloomReachJcrDocumentVariantType)
            .orElseThrow(() -> new RuntimeException("Variant " + bloomReachJcrDocumentVariantType.name() + " not found."));
    }

    public static Optional<Node> findDocumentVariantNode(final Node documentHandleNode,
                                                         final BloomReachJcrDocumentVariantType bloomReachJcrDocumentVariantType) {

        return getDocumentVariantNodesStream(documentHandleNode)
            .filter(node -> wrapCheckedException(() ->
                node.getProperty("hippostd:state").getString().equalsIgnoreCase(bloomReachJcrDocumentVariantType.name())
            ))
            .findFirst();
    }

    private static NodeIterator getDocumentVariantNodes(final Node documentHandleNode) {
        return wrapCheckedException(() -> documentHandleNode.getNodes(documentHandleNode.getName() + "*"));
    }

    private static Stream<Node> getDocumentVariantNodesStream(final Node documentHandleNode) {
        return streamOf(getDocumentVariantNodes(documentHandleNode));
    }

    public static Node getRootNode(final Session session) {
        final Node rootNode;
        try {
            rootNode = session.getRootNode();
        } catch (RepositoryException e) {
            throw new RuntimeException("Failed to obtain root node from session", e);
        }
        return rootNode;
    }

    public static Node getRelativeNode(final Node rootNode, final String jcrPath) {
        try {
            return rootNode.getNode(jcrPath);
        } catch (RepositoryException e) {
            throw new RuntimeException("Failed to obtain node " + jcrPath);
        }
    }

    public static String getStringProperty(final Node node, final String propertyName) {
        return findStringProperty(node, propertyName)
            .orElseThrow(() -> new NullPointerException("Property " + propertyName + " is not populated "));
    }

    public static Optional<String> findStringProperty(final Node node, final String propertyName) {
        return Optional.ofNullable(
            wrapCheckedException(() -> node.getProperty(propertyName).getString())
        );
    }

    public static Instant getInstantProperty(final Node node, final String propertyName) {
        return findInstantProperty(node, propertyName)
            .orElseThrow(() -> new NullPointerException("Property " + propertyName + " is not populated "));
    }

    public static Optional<Instant> findInstantProperty(final Node node, final String propertyName) {
        return findStringProperty(node, propertyName).map(Instant::parse);

    }

    @SuppressWarnings("unchecked") // it's guaranteed that NodeIterator operates on instances of Node class
    public static Stream<Node> streamOf(final NodeIterator nodeIterator) {

        return StreamSupport.stream(
            Spliterators.<Node>spliteratorUnknownSize(nodeIterator, Spliterator.ORDERED),
            false
        );
    }

    public static String getNameQuietly(final Node node) {
        return wrapCheckedException(node::getName);
    }

    public enum BloomReachJcrDocumentVariantType {
        DRAFT,
        UNPUBLISHED,
        PUBLISHED;
    }
}
