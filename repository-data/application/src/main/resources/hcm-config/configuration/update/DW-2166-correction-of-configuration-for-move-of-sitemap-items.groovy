package org.hippoecm.frontend.plugins.cms.admin.updater

import org.onehippo.repository.update.BaseNodeUpdateVisitor
import javax.jcr.Node
import javax.jcr.Property
import javax.jcr.RepositoryException
import javax.jcr.Session

class CorrectSitemapConfiguration extends BaseNodeUpdateVisitor {

    boolean logSkippedNodePaths() {
        return false // don't log skipped node paths
    }

    boolean skipCheckoutNodes() {
        return false // return true for readonly visitors and/or updates unrelated to versioned content
    }

    Node firstNode(final Session session) throws RepositoryException {
        return null // implement when using custom node selection/navigation
    }

    Node nextNode() throws RepositoryException {
        return null // implement when using custom node selection/navigation
    }

    boolean doUpdate(Node node) {
        log.debug "Updating node ${node.path}"

        replaceNode(node, "root", "nhsd-homepage")
        updateAboutConfiguration(node)
        replaceNode(node, "services", "nhsd-services-hub")
        replaceNode(node, "coronavirus", "nhsd-coronavirus")
        replaceNode(node, "cyber", "nhsd-cyber-hub")
        replaceNode(node, "data", "nhsd-data-hub")
        updateNewsConfiguration(node)
        updateDeveloperConfiguration(node)


        return false
    }

    void updateDeveloperConfiguration(Node node) {
        //updates properties instead of moving replacement to preserve children of original
        log.debug("updating developer configuration")

        if (!node.hasNode("developer")) {
            log.error("about not found")
            return
        }

        Node developer = node.getNode("developer");

        setNodeStringProperty(developer, "hst:componentconfigurationid", "hst:pages/nhsd-developer-hub")
        setNodeStringProperty(developer, "hst:relativecontentpath", "developer")

        removeChildNode(node, "nhsd-developer-hub")
        removeChildNode(developer, "_index_")
    }

    private void updateAboutConfiguration(Node node) {
        //updates properties instead of moving replacement to preserve children of original
        log.debug("updating about configuration")

        if (!node.hasNode("about")) {
            log.error("about not found")
            return
        }

        Node about = node.getNode("about");

        setNodeStringProperty(about, "hst:componentconfigurationid", "hst:pages/nhsd-about-us-hub")
        setNodeStringProperty(about, "hst:relativecontentpath", "about-nhs-digital")

        removeChildNode(node, "nhsd-about-us-hub")

    }

    void updateNewsConfiguration(Node node) {
        //updates properties instead of moving replacement to preserve children of original
        log.debug("updating news configuration")

        if (!node.hasNode("news")) {
            log.error("news not found")
            return
        }

        Node news = node.getNode("news");

        setNodeStringProperty(news, "hst:componentconfigurationid", "hst:pages/nhsd-news")
        setNodeStringProperty(news, "hst:relativecontentpath", "news")

        removeChildNode(node, "nhsd-news")
    }

    private void replaceNode(Node parent, String oldChild, String newChild) {
        if (!parent.hasNode(newChild)) {
            log.error("${newChild} not found on ${parent.path}")
            return
        }

        removeChildNode(parent, oldChild)
        parent.getSession().move(parent.getPath()+"/"+newChild, parent.getPath()+"/"+oldChild)
    }

    private void removeChildNode(Node parent, String child) {
        if (parent.hasNode(child)) {
            Node node = parent.getNode(child)
            node.remove()
        } else {
            log.info("${parent.path} has no ${child} node")
        }
    }
    private void setNodeStringProperty(Node node, String propertyName, String value) {
        if (node.hasProperty(propertyName)) {
            Property property = node.getProperty(propertyName)
            property.setValue(value)
        } else {
            log.error("${node.path} has no ${propertyName} property")
        }
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }

}
