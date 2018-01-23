package uk.nhs.digital.ps.modules;

import org.apache.jackrabbit.value.StringValue;
import org.onehippo.repository.events.HippoWorkflowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

public class FullTaxonomyModule extends AbstractDaemonModule {

    private static final Logger log = LoggerFactory.getLogger(FullTaxonomyModule.class);

    public static final String HIPPO_CLASSIFIABLE_PATH = "/hippo:namespaces/publicationsystem/publication/editor:templates/_default_/classifiable";
    public static final String TAXONOMY_NAME_PROPERTY = "essentials-taxonomy-name";
    public static final String FULL_TAXONOMY_PROPERTY = "common:FullTaxonomy";

    private Node taxonomyTreeNode;

    @Override
    public void initialize(final Session session) {
        super.initialize(session);

        this.taxonomyTreeNode = getTaxonomyTreeNode();
    }

    @Override
    protected void handleCommitEvent(HippoWorkflowEvent event) {

        try {
            Node node = getSession().getNode(event.subjectPath());

            // don't do the published ones, these will get populated when the document is published
            // also don't need to bother with node with no taxonomy
            streamChildNodes(node)
                .filter((document) -> !STATE_PUBLISHED.equals(getRepoValue(() -> document.getProperty("hippostd:state").getString())))
                .filter((document) -> getRepoValue(() -> document.hasProperty("hippotaxonomy:keys")))
                .forEach(this::createFullTaxonomyProperty);

            getSession().save();
        } catch (RepositoryException re) {
            throw new RuntimeException(re);
        }
    }

    private void createFullTaxonomyProperty(Node document) {
        Set<String> fullTaxonomyKeys = getFullTaxonomyKeys(document);
        Value[] fullTaxonomy = fullTaxonomyKeys.stream()
            .map(StringValue::new)
            .toArray(Value[]::new);

        try {
            document.setProperty(FULL_TAXONOMY_PROPERTY, fullTaxonomy);
        } catch (RepositoryException re) {
            log.error("Failed to set full taxonomy on node: " + document, re);
        }
    }

    private Set<String> getFullTaxonomyKeys(Node document) {
        Value[] values = getRepoValue(() -> document.getProperty("hippotaxonomy:keys").getValues());
        return Arrays.stream(values)
            .map(value -> getRepoValue(value::getString))
            .flatMap(this::getTaxonomyList)
            .collect(Collectors.toSet());
    }

    private Node getTaxonomyTreeNode() {
        return getRepoValue(() -> {
            String taxonomyName = getSession()
                .getNode(HIPPO_CLASSIFIABLE_PATH)
                .getProperty(TAXONOMY_NAME_PROPERTY)
                .getString();

            // get the published taxonomy
            return getSession().getNode("/content/taxonomies/" + taxonomyName + "/" + taxonomyName);
        });
    }

    private Stream<String> getTaxonomyList(String key) {

        Optional<Node> taxonomyNodeOptional = getDescendantTaxonomyNodes(taxonomyTreeNode)
            .filter(node -> getTaxonomyKey(node).equals(key))
            .findAny();

        if (!taxonomyNodeOptional.isPresent()) {
            log.error("Couldn't find taxonomy key in tree: " + key);
            return Stream.empty();
        }

        Node taxonomyNode = taxonomyNodeOptional.get();
        ArrayList<String> taxonomyKeys = new ArrayList<>();
        while (getRepoValue(taxonomyNode::getPrimaryNodeType)
            .isNodeType("hippotaxonomy:category")) {

            taxonomyKeys.add(getTaxonomyKey(taxonomyNode));
            taxonomyNode = getRepoValue(taxonomyNode::getParent);
        }

        return taxonomyKeys.stream();
    }

    private String getTaxonomyKey(Node node) {
        return getRepoValue(() -> node.getProperty("hippotaxonomy:key").getString());
    }

    private Stream<Node> getDescendantTaxonomyNodes(Node node) {
        return streamChildTaxonomyNodes(node)
            .flatMap(childNode -> Stream.concat(
                Stream.of(childNode),
                getDescendantTaxonomyNodes(childNode)
            ));
    }

    private Stream<Node> streamChildTaxonomyNodes(Node parentNode) {
        return streamChildNodes(parentNode)
            .filter((childNode) -> getRepoValue(childNode::getPrimaryNodeType)
                .isNodeType("hippotaxonomy:category")
            );
    }

    private Stream<Node> streamChildNodes(Node parentNode) {
        return StreamSupport.stream(
            ((Iterable<Node>) () -> getRepoValue(parentNode::getNodes)).spliterator(),
            false
        );
    }

    /**
     * Handling for repository operations that throw exceptions
     */
    private static <T> T getRepoValue(RepositoryOperation<T> repositoryOperation) {
        try {
            return repositoryOperation.get();
        } catch (RepositoryException re) {
            throw new RuntimeException("Repository operation failed", re);
        }
    }

    @FunctionalInterface
    private interface RepositoryOperation<T> {
        T get() throws RepositoryException;
    }
}
