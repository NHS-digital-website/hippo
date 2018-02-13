package org.hippoecm.frontend.plugins.cms.admin.updater

import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.Session
/**
 * This migration makes the publication system folders restricted to the publicationsystem doc types
 */
class Migration20180213RestrictedFolderType extends BaseNodeUpdateVisitor {

    Session session

    void initialize(Session session) {
        this.session = session
    }

    boolean doUpdate(Node node) {
        try {
            if ("hippostd:folder".equals(node.getPrimaryNodeType().getName())) {
                def path = node.getPath()
                def folderTypes = getFolderTypesForPath(path)
                if (folderTypes != null) {
                    log.debug("Converting " + path + " to types " + folderTypes)
                    node.setProperty("hippostd:foldertype", getFolderTypesForPath(path))
                    return true
                }
            }
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    private static String[] getFolderTypesForPath(String path) {
        if (path.startsWith("/content/documents/corporate-website/publication-system/ci-hub")) {
            return ["new-cihub-document"]
        } else if (path.startsWith("/content/documents/corporate-website/publication-system")) {
            return ["new-statistical-publications-and-clinical-indicators-document", "new-statistical-publications-and-clinical-indicators-folder"]
        } else if (path.startsWith("/content/documents/corporate-website/about")) {
            return ["new-about-document"]
        } else if (path.equals("/content/documents/corporate-website")) {
            return ["new-translated-folder"]
        } else {
            return null
        }
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException();
    }
}
