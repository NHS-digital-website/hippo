
import org.apache.commons.lang.text.StrSubstitutor
import org.apache.commons.vfs2.FileObject
import org.apache.commons.vfs2.VFS
import org.hippoecm.repository.api.HippoWorkspace
import org.hippoecm.repository.api.WorkflowManager
import org.onehippo.forge.content.exim.core.ContentMigrationRecord
import org.onehippo.forge.content.exim.core.DocumentManager
import org.onehippo.forge.content.exim.core.impl.WorkflowDocumentManagerImpl
import org.onehippo.forge.content.exim.core.impl.WorkflowDocumentVariantImportTask
import org.onehippo.forge.content.exim.core.util.HippoNodeUtils
import org.onehippo.forge.content.pojo.model.ContentNode
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.Session

/**
 * This script consumes content generated by
 * <a href="https://github.com/NHS-digital-website/indicators-hippo-migrator">Indicators Migrator</a>.
 *
 */
class MigratorImporterScript extends BaseNodeUpdateVisitor {

    DocumentManager documentManager
    WorkflowDocumentVariantImportTask importTask
    FileObject sourceBaseFolder
    Session session

    WorkflowManager workflowManager

    void initialize(Session session) {
        this.session = session

        def sourceBaseFolderPath = StrSubstitutor.replaceSystemProperties(parametersMap.get("sourceBaseFolderPath"))
        sourceBaseFolder = VFS.getManager().resolveFile(sourceBaseFolderPath)

        workflowManager = ((HippoWorkspace)session.getWorkspace()).getWorkflowManager()

        documentManager = new WorkflowDocumentManagerImpl(session)
        importTask = new WorkflowDocumentVariantImportTask(documentManager)
        importTask.setLogger(log)
        importTask.start()
    }

    boolean doUpdate(Node node) {

        // This method is called for each node in the jcr,
        // we obviously only want to do the import once so only do it for the root directory
        if (!node.getPath().equals("/content/documents/corporate-website/publication-system")) {
            return false
        }

        FileObject[] files = findImportFiles()

        if (!files) {
            log.error "No files found to import in ${sourceBaseFolder}. Aborting with no attempt of importing anything having been made."
            return false
        }

        log.info "Files to import found: ${files.length}"

        files
            .toSorted { left, right -> left.getName().compareTo(right.getName()) }
            .eachWithIndex { FileObject file, int i ->

            log.info "Processing file [${i + 1}/${files.length}]: ${file.name.path}"

            ContentNode contentNode = importTask.readContentNodeFromJsonFile(file)

            String primaryTypeName = contentNode.getPrimaryType()

            String nodeFullPath = contentNode.getProperty("jcr:path").getValue()

            String localizedName = contentNode.getProperty("jcr:localizedName").getValue()


            if ("hippostd:folder".equals(primaryTypeName)) {

                createFolder(nodeFullPath, primaryTypeName, localizedName, file)

            } else {

                createDocument(nodeFullPath, primaryTypeName, contentNode, localizedName, file)

                publishDocument(nodeFullPath, primaryTypeName, file)
            }

        }

        return true
    }

    private createFolder(final String nodeFullPath, final String primaryTypeName, final String localizedName, final FileObject file) {
        prepareBatchUnit(nodeFullPath, primaryTypeName, {
            log.info("Creating ${primaryTypeName}: ${nodeFullPath}")

            Node folderNode = HippoNodeUtils.createMissingHippoFolders(session, nodeFullPath)

            folderNode.addMixin("hippo:named")
            folderNode.setProperty("hippo:name", localizedName)
            log.info("Created ${primaryTypeName}: ${nodeFullPath}")

        }, file.name.path)
    }

    private void createDocument(final String nodeFullPath, final String primaryTypeName, final ContentNode contentNode, final String localizedName, final FileObject file) {
        prepareBatchUnit(nodeFullPath, primaryTypeName, {
            log.info("Creating ${primaryTypeName}: ${nodeFullPath}")

            String locale = (contentNode.hasProperty("hippotranslation:locale")) ? contentNode.getProperty("hippotranslation:locale").getValue() : null

            String updatedDocumentLocation = importTask.createOrUpdateDocumentFromVariantContentNode(
                contentNode,
                primaryTypeName,
                nodeFullPath,
                locale,
                localizedName
            )

            log.info("Created ${primaryTypeName}: ${updatedDocumentLocation}")
        }, file.name.path)
    }

    private void publishDocument(final String nodeFullPath, final String primaryTypeName, final FileObject file) {
        prepareBatchUnit(nodeFullPath, primaryTypeName, {
            log.info("Publishing ${primaryTypeName}: ${nodeFullPath}")

            documentManager.publishDocument(nodeFullPath)

            log.info("Published ${primaryTypeName}: ${nodeFullPath}")
        }, file.name.path)
    }


    def prepareBatchUnit(String nodeFullPath, String primaryTypeName, Closure actionClosure, String importFilePath) {

        ContentMigrationRecord contentMigrationRecord
        try {

            contentMigrationRecord = importTask.beginRecord("", nodeFullPath)
            contentMigrationRecord.setProcessed(true)
            contentMigrationRecord.setAttribute("file", importFilePath)
            contentMigrationRecord.setContentType(primaryTypeName)

            actionClosure()

            visitorContext.reportUpdated(nodeFullPath)
            contentMigrationRecord.setSucceeded(true)

        } catch (e) {
            log.error("Failed to import ${primaryTypeName}: ${nodeFullPath}", e)
            visitorContext.reportFailed(nodeFullPath)
            contentMigrationRecord.setErrorMessage(e.toString())
        } finally {
            importTask.endRecord()
        }
    }



    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }

    void destroy() {
        importTask.stop()
        importTask.logSummary()
    }

    private FileObject[] findImportFiles() {
        FileObject[] files = importTask.findFilesByNamePattern(sourceBaseFolder, "^.+\\.json\$", 1, 10)

        return files == null ? [] : files;
    }
}
