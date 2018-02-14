package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.forge.content.exim.core.DocumentManager
import org.onehippo.forge.content.exim.core.impl.WorkflowDocumentManagerImpl
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.Session

/**
 * This migration causes the Full Taxonomy to get populated by commiting and publishing
 * every publication and dataset
 */
class Migration20180216FacetType extends BaseNodeUpdateVisitor {

    static final String PROPERTY_FACET_TYPE = "common:FacetType"

    HashMap facetTypes = [
        "publicationsystem:publication": "publication",
        "publicationsystem:legacypublication": "publication",
        "publicationsystem:series": "series",
        "publicationsystem:archive": "series",
        "publicationsystem:dataset": "dataset"
    ]

    Session session
    DocumentManager documentManager

    void initialize(Session session) {
        this.session = session
    }

    boolean doUpdate(Node node) {
        try {
            if (facetTypes.containsKey(node.getPrimaryNodeType().getName())) {
                log.info("attempting to migrate node: " + node.getPath())
                return addFacetTypeProperty(node)
            }
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean undoUpdate(Node node) {
        try {
            if (facetTypes.containsKey(node.getPrimaryNodeType().getName())) {
                log.info("attempting to undo migration for node: " + node.getPath())
                return removeFacetTypeProperty(node)
            }
        } catch (e) {
            log.error("Failed to (undo) process record.", e)
        }

        return false
    }

    boolean addFacetTypeProperty(Node node) {
        String primaryType = node.getPrimaryNodeType().getName()
        String facetType = facetTypes.get(primaryType)

        JcrUtils.ensureIsCheckedOut(node)
        node.setProperty(PROPERTY_FACET_TYPE, facetType)
        session.save()
        log.info(primaryType + " marked as " + facetType)

        return true
    }

    boolean removeFacetTypeProperty(Node node) {
        JcrUtils.ensureIsCheckedOut(node)
        if (node.hasProperty(PROPERTY_FACET_TYPE)) {
            node.getProperty(PROPERTY_FACET_TYPE).remove()
            session.save()
            log.info("removed property " + PROPERTY_FACET_TYPE)
        }

        return true
    }
}
