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
class Migration20180122FullTaxonomy extends BaseNodeUpdateVisitor {

    List typesWithTaxonomy = [
        "publicationsystem:publication",
        "publicationsystem:dataset"
    ]

    Session session
    DocumentManager documentManager

    void initialize(Session session) {
        this.session = session
        this.documentManager = new WorkflowDocumentManagerImpl(session)
    }

    boolean doUpdate(Node node) {
        try {
            // Just the document nodes
            if ("hippo:handle".equals(node.getPrimaryNodeType().getName())) {
                if (node.hasNode(node.getName())) {
                    Node subNode = node.getNode(node.getName());
                    if (typesWithTaxonomy.contains(subNode.getPrimaryNodeType().getName())) {
                        log.info("attempting to migrate node: " + node.getPath())
                        return saveAndPublishDocument(node)
                    }
                }
            }
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean undoUpdate(Node node) {
        try {
            if (typesWithTaxonomy.contains(node.getPrimaryNodeType().getName())) {
                log.info("attempting to unmigrate node: " + node.getPath())

                if (node.hasProperty("common:FullTaxonomy")) {
                    node.getProperty("common:FullTaxonomy").remove()
                }

                return true
            }
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean saveAndPublishDocument(Node node) {
        String path = node.getPath()

        // Change and commit the document to trigger the event bus to generate the taxonomy
        Node documentNode = documentManager.obtainEditableDocument(path).getNode(session)
        JcrUtils.ensureIsCheckedOut(documentNode);
        documentNode.setProperty("hippostdpubwf:lastModificationDate", Calendar.getInstance())
        session.save()
        documentManager.commitEditableDocument(path)
        session.save()

        // Publish the document to push the new taxonomy out
        documentManager.publishDocument(path)
        session.save()

        return true
    }
}
