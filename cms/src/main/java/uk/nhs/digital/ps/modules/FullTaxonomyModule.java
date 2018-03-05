package uk.nhs.digital.ps.modules;

import static java.util.stream.Collectors.toSet;
import static org.apache.cxf.common.util.CollectionUtils.isEmpty;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.jackrabbit.value.StringValue;
import org.hippoecm.repository.util.JcrUtils;
import org.onehippo.repository.events.HippoWorkflowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

public class FullTaxonomyModule extends AbstractDaemonModule {

    private static final Logger log = LoggerFactory.getLogger(FullTaxonomyModule.class);

    public static final String HIPPO_CLASSIFIABLE_PATH = "/hippo:namespaces/publicationsystem/publication/editor:templates/_default_/classifiable";

    public static final String FULL_TAXONOMY_PROPERTY = "common:FullTaxonomy";
    public static final String SEARCHABLE_TAGS_PROPERTY = "common:SearchableTags";

    public static final String TAXONOMY_NODE_NAME_PROPERTY = "essentials-taxonomy-name";
    public static final String TAXONOMY_CATEGORY_NODE_TYPE = "hippotaxonomy:category";

    public static final String TAXONOMY_CATEGORY_INFOS_PROPERTY = "hippotaxonomy:categoryinfos";
    public static final String TAXONOMY_NAME_PROPERTY = "hippotaxonomy:name";
    public static final String TAXONOMY_KEY_PROPERTY = "hippotaxonomy:key";
    public static final String TAXONOMY_KEYS_PROPERTY = "hippotaxonomy:keys";
    public static final String TAXONOMY_SYNONYMS_PROPERTY = "hippotaxonomy:synonyms";

    @Override
    protected void handleCommitEvent(HippoWorkflowEvent event) {

        try {
            Node node = getSession().getNode(event.subjectPath());

            // don't do the published ones, these will get populated when the document is published
            // also don't need to bother with node with no taxonomy
            streamChildNodes(node)
                .filter((document) -> !STATE_PUBLISHED.equals(getRepoValue(() -> document.getProperty("hippostd:state").getString())))
                .filter((document) -> getRepoValue(() -> document.hasProperty(TAXONOMY_KEYS_PROPERTY)))
                .forEach(this::process);

            getSession().save();
        } catch (RepositoryException re) {
            throw new RuntimeException(re);
        }
    }

    private void process(Node node) {
        createFullTaxonomyProperty(node);
        createSearchableTagsProperty(node);
    }

    protected void createSearchableTagsProperty(Node document) {
        Value[] values = getRepoValue(() -> document.getProperty(TAXONOMY_KEYS_PROPERTY).getValues());
        Set<String> keys = Arrays.stream(values)
            .map(value -> getRepoValue(value::getString))
            .collect(toSet());

        Set<String> searchableTags = getTaxonomyTermsAndSynonyms(keys);

        if (!isEmpty(searchableTags)) {
            try {
                JcrUtils.ensureIsCheckedOut(document);
                document.setProperty(SEARCHABLE_TAGS_PROPERTY, stringsToValues(searchableTags));
            } catch (RepositoryException re) {
                log.error("Failed to set searchable tags on node: " + document, re);
            }
        }
    }

    private Set<String> getTaxonomyTermsAndSynonyms(Set<String> keys) {
        return getDescendantTaxonomyNodes(getTaxonomyTreeNode())
            .filter(node -> keys.contains(getTaxonomyKey(node)))
            .flatMap(this::getTermsAndSynonyms)
            .collect(toSet());
    }

    private Stream<String> getTermsAndSynonyms(Node taxonomyNode) {
        return getRepoValue(() -> {
            Node info = taxonomyNode.getNode(TAXONOMY_CATEGORY_INFOS_PROPERTY).getNode("en");

            String taxonomyName = info.getProperty(FullTaxonomyModule.TAXONOMY_NAME_PROPERTY).getString();
            Stream<String> stream = Stream.of(taxonomyName);

            if (info.hasProperty(TAXONOMY_SYNONYMS_PROPERTY)) {
                stream = Stream.concat(
                    stream,
                    Arrays.stream(info.getProperty(TAXONOMY_SYNONYMS_PROPERTY).getValues())
                        .map(value -> getRepoValue(value::getString))
                );
            }

            return stream;
        });
    }

    protected void createFullTaxonomyProperty(Node document) {
        Set<String> fullTaxonomyKeys = getFullTaxonomyKeys(document);
        Value[] fullTaxonomy = stringsToValues(fullTaxonomyKeys);

        // We have seen this happen when a document has only invalid taxonomy keys
        // Don't create the new property in this case
        if (ArrayUtils.isEmpty(fullTaxonomy)) {
            return;
        }

        try {
            JcrUtils.ensureIsCheckedOut(document);
            document.setProperty(FULL_TAXONOMY_PROPERTY, fullTaxonomy);
        } catch (RepositoryException re) {
            log.error("Failed to set full taxonomy on node: " + document, re);
        }
    }

    private Value[] stringsToValues(Set<String> strings) {
        return strings.stream()
            .map(StringValue::new)
            .toArray(Value[]::new);
    }

    private Set<String> getFullTaxonomyKeys(Node document) {
        Value[] values = getRepoValue(() -> document.getProperty(TAXONOMY_KEYS_PROPERTY).getValues());
        return Arrays.stream(values)
            .map(value -> getRepoValue(value::getString))
            .flatMap(this::getTaxonomyList)
            .collect(toSet());
    }

    private Node getTaxonomyTreeNode() {
        return getRepoValue(() -> {
            String taxonomyName = getSession()
                .getNode(HIPPO_CLASSIFIABLE_PATH)
                .getProperty(TAXONOMY_NODE_NAME_PROPERTY)
                .getString();

            // get the published taxonomy
            return getSession().getNode("/content/taxonomies/" + taxonomyName + "/" + taxonomyName);
        });
    }

    private Stream<String> getTaxonomyList(String key) {

        Optional<Node> taxonomyNodeOptional = getDescendantTaxonomyNodes(getTaxonomyTreeNode())
            .filter(node -> getTaxonomyKey(node).equals(key))
            .findAny();

        if (!taxonomyNodeOptional.isPresent()) {
            log.error("Couldn't find taxonomy key in tree: " + key);
            return Stream.empty();
        }

        Node taxonomyNode = taxonomyNodeOptional.get();
        ArrayList<String> taxonomyKeys = new ArrayList<>();
        while (getRepoValue(taxonomyNode::getPrimaryNodeType)
            .isNodeType(TAXONOMY_CATEGORY_NODE_TYPE)) {

            taxonomyKeys.add(getTaxonomyKey(taxonomyNode));
            taxonomyNode = getRepoValue(taxonomyNode::getParent);
        }

        return taxonomyKeys.stream();
    }

    private String getTaxonomyKey(Node node) {
        return getRepoValue(() -> node.getProperty(TAXONOMY_KEY_PROPERTY).getString());
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
                .isNodeType(TAXONOMY_CATEGORY_NODE_TYPE)
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
