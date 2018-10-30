package uk.nhs.digital.ps.workflow.searchableTaxonomy;

import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.NodeIterator
import javax.jcr.Session

/**
 * This migration updates "SearchableTaxonomy" and "FullTaxonomy" fields
 */
class HotFixSearchableAndFullTaxonomy extends BaseNodeUpdateVisitor {
    def allowedTypes = [
        "publicationsystem:publication",
        "publicationsystem:dataset",
        "publicationsystem:legacypublication"
    ]

    SearchableTaxonomyTaskCopy task
    Session session

    private static final String TAXONOMY_KEYS_PROPERTY = "hippotaxonomy:keys"

    boolean doUpdate(Node node) {
        session = node.getSession()
        task = new SearchableTaxonomyTaskCopy()
        task.hookSetSession(session)

        try {
            // make sure it's "handle"
            if (!"hippo:handle".equals(node.getPrimaryNodeType().getName())) {
                return skipNodeWrongType(node)
            }

            // make sure it's allowed Type
            if (!allowedTypes.contains(node.getNode(node.getName()).getPrimaryNodeType().getName())) {
                return skipNodeWrongPrimaryType(node)
            }

            NodeIterator i = node.getNodes()
            Node n

            log.info("Updating " + node.getPath())
            while(i.hasNext()) {
                n = i.nextNode()

                if (! n.hasProperty(TAXONOMY_KEYS_PROPERTY)) {
                    log.info("No taxonomy keys, skipping")
                    continue
                }

                log.warn("SearchableTaxonomy " + n.getProperty("hippostd:state").getString())
                task.hookCreateSearchableTagsProperty(n)

                log.warn("FullTaxonomy " + n.getProperty("hippostd:state").getString())
                task.hookCreateFullTaxonomyProperty(n)
            }

            return true
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean undoUpdate(Node node) {
        log.error("UNDO is not available")
        return false
    }

    boolean skipNodeWrongType(Node node) {
        log.warn("Wrong type, skipping document " + node.getPath() + ", of type " + node.getPrimaryNodeType().getName());

        return false
    }

    boolean skipNodeWrongPrimaryType(Node node) {
        log.warn("Wrong primary type, skipping document " + node.getPath() + ", of type " + node.getPrimaryNodeType().getName());

        return false
    }

    class SearchableTaxonomyTaskCopy extends SearchableTaxonomyTask {
        void hookCreateSearchableTagsProperty(Node document) {
            createSearchableTagsProperty(document)
        }

        void hookCreateFullTaxonomyProperty(Node document) {
            createFullTaxonomyProperty(document)
        }

        void hookSetSession(Session session) {
            super.setSession(session)
        }
    }
}
