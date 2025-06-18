package org.example.updater

import javax.jcr.Node
import javax.jcr.Session
import javax.jcr.NodeIterator
import org.onehippo.repository.update.BaseNodeUpdateVisitor

class RemoveEnableRapiDocField extends BaseNodeUpdateVisitor {

    int totalDocuments = 0
    int removedCount = 0
    String basePath = "/content/documents/corporate-website/developer/api-catalogue"

    @Override
    boolean doUpdate(Node node) {
        try {
            if (node.getPath().startsWith(basePath) && node.hasProperty("website:enable_rapidoc")) {
                totalDocuments++
                node.getProperty("website:enable_rapidoc").remove()
                removedCount++
                log.info("Removed enable_rapidoc from: ${node.path}")
            }
        } catch (Exception e) {
            log.error("Failed to remove enable_rapidoc from: ${node.path}", e)
            return false
        }
        return true
    }

    @Override
    void destroy() {
        log.info("Total documents found with enable_rapidoc: ${totalDocuments}")
        log.info("Successfully removed enable_rapidoc from: ${removedCount} documents.")
        log.info("Operation completed.")
    }

    @Override
    boolean undoUpdate(Node node) {
        return false
    }
}