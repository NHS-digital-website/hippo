package org.hippoecm.frontend.plugins.cms.admin.updater

import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.Session
/**
 * Update restricts corporate website folders to specific doc types
 */
class RestrictFolderAllowedDocumentTypes extends BaseNodeUpdateVisitor {

    private static final String CW_ROOT_FOLDER = "/content/documents/corporate-website/"
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
                    log.debug("Converting " + path)
                    node.setProperty("hippostd:foldertype", folderTypes)
                    return true
                }
            }
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    private static String[] getFolderTypesForPath(String path) {
        if (isDigitalWebsitePath(path)) {
            return ["new-digital-website-document", "new-digital-website-folder"]
        } else {
            // other folders already updated from Migration20180213RestrictedFolderType
            return null
        }
    }

    private static boolean isDigitalWebsitePath(String path) {
        return (path.startsWith(CW_ROOT_FOLDER + "systems-and-services")
            || path.startsWith(CW_ROOT_FOLDER + "services")
            || path.startsWith(CW_ROOT_FOLDER + "general")
            || path.startsWith(CW_ROOT_FOLDER + "hubs")
            || path.startsWith(CW_ROOT_FOLDER + "data-and-information")
            || path.startsWith(CW_ROOT_FOLDER + "about-nhs-digital")
            || path.startsWith(CW_ROOT_FOLDER + "news-and-events")
            || path.startsWith(CW_ROOT_FOLDER + "hidden-articles")
            || path.startsWith(CW_ROOT_FOLDER + "what-is-nhs-digital")
            || path.startsWith(CW_ROOT_FOLDER + "cta")
            || path.startsWith(CW_ROOT_FOLDER + "blog")
            || path.startsWith(CW_ROOT_FOLDER + "banners")
        )
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException();
    }
}
