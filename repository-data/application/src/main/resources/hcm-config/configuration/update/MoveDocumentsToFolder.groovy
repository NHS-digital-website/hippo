import org.hippoecm.repository.api.HippoWorkspace
import org.hippoecm.repository.api.Workflow
import org.hippoecm.repository.api.WorkflowManager
import org.onehippo.forge.content.exim.core.DocumentManager
import org.onehippo.forge.content.exim.core.impl.WorkflowDocumentManagerImpl
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session

/**
 * <p>
 * Moves documents identified by the '{@code query}' config settings to a folder pointed to by
 * '{@code targetFolderPath} parameter.
 * </p>
 * <strong>IMPORTANT</strong>:
 * <ul>
 *     <li>
 *         Ensure that the query targets nodes of type '{@code hippo:handle}' as, in its current
 *         version the script, does not validate them (it's likely that it'll equally well
 *         handle '{@code hippostd:folder}' nodes but this hasn't been tesed).
 *     </li>
 *     <li>
 *         The script does not support a 'Dry run' as it relies on {@linkplain Workflow#move} method
 *         to relocate documents and this results in a session being committed, effectively
 *         invalidating the dry run. If you do want to do a dry run, comment out the call to
 * {@linkplain Workflow#move}.
 *     </li>
 *     <li>
 *         The script does not implement '{@code undoUpdate}' method (note that clicking 'Undo'
 *         feeds the method with nodes identified by the original query - in this case, the query
 *         will not find anything as the nodes it returned previously will have already been
 *         moved by this script). Having said that, it logs the new paths mapped to the original
 *         locations (parent folders) of the moved documents to help identify what needs to be
 *         moved back and where to.
 *     </li>
 * </ul>
 */
class MoveDocumentsToFolder extends BaseNodeUpdateVisitor {

    WorkflowManager workflowManager
    DocumentManager documentManager
    Session session
    String targetFolderPath

    int documentsCount

    def newPathsToOldLocations = [:]

    @Override
    void initialize(final Session session) throws RepositoryException {
        workflowManager = ((HippoWorkspace) session.getWorkspace()).getWorkflowManager()
        documentManager = new WorkflowDocumentManagerImpl(session)

        targetFolderPath = parametersMap.get("targetFolderPath")
        log.info("Target folder: ${targetFolderPath}")
        this.session = session;
    }

    boolean doUpdate(final Node node) {
        documentsCount++

        try {
            log.info("Moving document ${documentsCount}: ${node.getPath()}")

            final String oldParentFolderPath = node.getParent().getPath()

            final String newDocumentPath = moveDocument(oldParentFolderPath, targetFolderPath, node.getName())

            newPathsToOldLocations.put(newDocumentPath, oldParentFolderPath)

            log.info("Done; current document path is: ${newDocumentPath}")

        } catch (e) {
            log.error("Failed to move document ${node.getPath()}.", e)
        }

        return true
    }

    private String moveDocument(String oldParentFolderPath, String targetFolderPath, String nodename) {
        log.info("Source Fodler ----->:   ${oldParentFolderPath}")
        log.info("Target Folder : ${targetFolderPath}")

        session.move(oldParentFolderPath + "/" + nodename, "${targetFolderPath}/${nodename}")


        return null;
    }

    boolean undoUpdate(final Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }

    @Override
    void destroy() {
        log.info("Moved ${documentsCount} document(s) to ${targetFolderPath}")
        log.info("New documents' paths mapped to old their old locations (folders):\n${newPathsToOldLocations}")
    }
}
