package org.hippoecm.frontend.plugins.cms.admin.updater

import org.apache.commons.lang.text.StrSubstitutor
import org.apache.commons.vfs2.FileObject
import org.apache.commons.vfs2.VFS
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session

/**
 * Moves the content of a source folder into a target folder. Any preexisting content
 * in the target folder will remain untouched.
 */
class FolderMerger extends BaseNodeUpdateVisitor {

    Node targetFolderNode

    @Override
    public void initialize(Session session) throws RepositoryException {
        def targetFolderPath = StrSubstitutor.replaceSystemProperties(parametersMap.get("targetFolderPath"))
        if(session.nodeExists(targetFolderPath)){
            targetFolderNode = session.getNode(targetFolderPath);
        } else {
            throw new RepositoryException("Node " + targetFolderPath + " does not exist")
        }
    }


    @Override
    boolean doUpdate(Node node) throws RepositoryException {
        if ("hippostd:folder".equals(node.getPrimaryNodeType().getName()) && "hippostd:folder".equals(targetFolderNode.getPrimaryNodeType().getName())) {
            node <-- targetFolderNode.getNodes()
            return true;
        } else {
            throw new RepositoryException(node.getName() + " and/or " + targetFolderNode.getNode() + " are/is not a folder")
        }
        return false
    }

    @Override
    boolean undoUpdate(Node node) throws RepositoryException, UnsupportedOperationException {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }
}
