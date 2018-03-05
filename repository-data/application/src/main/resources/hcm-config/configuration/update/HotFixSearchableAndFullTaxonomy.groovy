package uk.nhs.digital.ps.modules

import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.*
import javax.jcr.Node

/**
 * This migration updates "SearchableTaxonomy" and "FullTaxonomy" fields
 */
class HotFixSearchableAndFullTaxonomy extends BaseNodeUpdateVisitor {
    def allowedTypes = [
        "publicationsystem:publication",
        "publicationsystem:dataset"
    ]

    TaxonomyDaemonCopy daemonCopy
    Session session

    private static final String TAXONOMY_KEYS_PROPERTY = "hippotaxonomy:keys"

    boolean doUpdate(Node node) {
        session = node.getSession()
        daemonCopy = new TaxonomyDaemonCopy(session)

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
                daemonCopy.hookCreateSearchableTagsProperty(n)

                log.warn("FullTaxonomy " + n.getProperty("hippostd:state").getString())
                daemonCopy.hookCreateFullTaxonomyProperty(n)
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

    class TaxonomyDaemonCopy extends FullTaxonomyModule {
        Session session

        TaxonomyDaemonCopy(Session session) {
            this.session = session
        }

        public Session getSession() {
            return session
        }

        public void hookCreateSearchableTagsProperty(Node document) {
            createSearchableTagsProperty(document)
        }

        public void hookCreateFullTaxonomyProperty(Node document) {
            createFullTaxonomyProperty(document)
        }
    }
}
