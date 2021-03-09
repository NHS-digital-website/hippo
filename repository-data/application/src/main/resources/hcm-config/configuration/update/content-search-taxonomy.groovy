package org.hippoecm.frontend.plugins.cms.admin.updater

import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

class UpdaterTemplate extends BaseNodeUpdateVisitor {

    boolean doUpdate(Node node) {

        if (node != null && node.hasProperty("hippotaxonomy:keys") && node.getProperty("hippotaxonomy:keys").getValues() != null) {
            if (node.getPrimaryNodeType().getName().split(":")[0].equals("website")) {
                node.setProperty("website:taxonomyClassificationField", node.getProperty("hippotaxonomy:keys").getValues());
                return true
            } else if (node.getPrimaryNodeType().getName().split(":")[0].equals("publicationsystem") || node.getPrimaryNodeType().getName().split(":")[0].equals("nationalindicatorlibrary")) {
                node.setProperty("common:taxonomyClassificationField", node.getProperty("hippotaxonomy:keys").getValues())
                return true
            }
        }
        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }

}
