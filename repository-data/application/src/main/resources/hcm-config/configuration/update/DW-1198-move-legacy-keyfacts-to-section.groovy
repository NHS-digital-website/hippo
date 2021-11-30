package org.hippoecm.frontend.plugins.cms.admin.updater

import org.apache.commons.lang.StringUtils
import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

class MoveLegacyKeyfactsToSection extends BaseNodeUpdateVisitor {

    def subNodes = [
        "publicationsystem:KeyFacts"
    ];

    boolean doUpdate(Node node) {
        log.debug "Updating node ${node.path}"
        try {
            if (node.hasNodes()) {
                return updateNode(node)
            }
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean updateNode(Node publicationNode) {

        def path = publicationNode.getPath()
        def nodeType = publicationNode.getPrimaryNodeType().getName()

        log.info("attempting to update node: " + path + " => current node type: " + nodeType)

        subNodes.eachWithIndex{ String subNode, i ->

            if (publicationNode.hasNode(subNode)) {
                if(StringUtils.isNotEmpty(publicationNode.getNode(subNode).getProperty("hippostd:content").toString())) {
                        def body = publicationNode.getNode(subNode)
                        Node section = publicationNode.addNode("website:sections","website:section")
                        Node bodyNode = section.addNode("website:html", "hippostd:html")
                        section.setProperty("website:numberedList", false)
                        section.setProperty("website:title", "")
                        section.setProperty("website:type", "")

                        bodyNode.setProperty("hippostd:content", JcrUtils.getStringProperty(body, "hippostd:content", ""))

                        if(body.getNodes().hasNext()) {
                            JcrUtils.copy(body.getNodes().nextNode(), body.getNodes().nextNode().getName(), bodyNode)
                        }

                        body.setProperty("hippostd:content", "")
                    }
                }

                log.info("Finished updating")
            }

        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }
}
