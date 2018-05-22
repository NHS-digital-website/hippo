package eximgroovy

import org.apache.commons.lang.text.StrSubstitutor
import org.apache.commons.vfs2.FileObject
import org.apache.commons.vfs2.VFS
import org.hippoecm.repository.api.HippoWorkspace
import org.hippoecm.repository.api.WorkflowManager

import org.hippoecm.repository.api.*
import org.onehippo.forge.content.exim.core.*
import org.onehippo.forge.content.exim.core.impl.*
import org.onehippo.forge.content.exim.core.util.*
import org.onehippo.forge.content.pojo.model.*
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.Session

/**
 * Create copies of general documents as news documents.
 xpath example :
 /jcr:root/content/documents/corporate-website/general//element(*, website:general)[@hippostd:state = 'draft']
 Example properties;
 {"replacePathFrom": "/content/documents/corporate-website/general"
 , "replacePathTo": "content/documents/corporate-website/general2"
 }
 *
 */
class CreateNewsDocuments extends BaseNodeUpdateVisitor {

    DocumentManager documentManager
    WorkflowDocumentVariantImportTask importTask
    WorkflowDocumentVariantExportTask exportTask
    String replacePathFrom
    String replacePathTo
    Session session

    WorkflowManager workflowManager

    void initialize(Session session) {
        this.session = session

        replacePathFrom = StrSubstitutor.replaceSystemProperties(parametersMap.get("replacePathFrom"))
        replacePathTo = StrSubstitutor.replaceSystemProperties(parametersMap.get("replacePathTo"))

        workflowManager = ((HippoWorkspace)session.getWorkspace()).getWorkflowManager()

        documentManager = new WorkflowDocumentManagerImpl(session)

        importTask = new WorkflowDocumentVariantImportTask(documentManager)
        exportTask = new WorkflowDocumentVariantExportTask(documentManager)
        importTask.setLogger(log)
        exportTask.setLogger(log)
        importTask.start()
        exportTask.start()
    }

    boolean doUpdate(Node node) {

        log.debug "From: ${node.path}"
        String to = node.path.replace(replacePathFrom, replacePathTo)
        log.debug "To: ${to}"

        def handlePath = node.parent.path
        def document = new Document(node.identifier)
        def contentNode = exportTask.exportVariantToContentNode(document)

        ContentNode newContentNode = new ContentNode(contentNode.getName(), "website:news")

        // Get properties required for the create
        String locale = (contentNode.hasProperty("hippotranslation:locale")) ? contentNode.getProperty("hippotranslation:locale").getValue() : null
        String primaryTypeName = "website:news"
        String localizedName = contentNode.getProperty("jcr:localizedName").getValue()

        // Update jcr:path
        String nodeFullPath = contentNode.getProperty("jcr:path").getValue()
        nodeFullPath = nodeFullPath.replace(replacePathFrom, replacePathTo)

        // Copy properties
        List<ContentProperty> oldProperties = contentNode.getProperties()
        for(ContentProperty oldProperty : oldProperties){
            newContentNode.setProperty(oldProperty)
        }
        newContentNode.getProperty("jcr:path").setValue(nodeFullPath)

        // Create body ContentNode
        ContentNode bodyNode = new ContentNode("website:body", "hippostd:html")

        // Get concatenated sections
        String bodyContent = ""
        List<ContentNode> contentNodeList = contentNode.getNodes()
        for(ContentNode n : contentNodeList){

            if("website:sections".equals(n.getName())) {
                String sectionTitle = ""
                if(null != n.getProperty("website:title")){
                    sectionTitle = n.getProperty("website:title").getValue()
                }

                String html = ""
                ContentNode sectionHtml = n.getNode("website:html")
                if(null != sectionHtml){
                    ContentProperty contentProperty = n.getNode("website:html").getProperty("hippostd:content")
                    if(null != contentProperty){
                        html = contentProperty.getValue()
                    }
                }
                bodyContent = bodyContent + html
            }
        }

        // Add the body node to the new document
        bodyNode.setProperty("hippostd:content", bodyContent)
        newContentNode.addNode(bodyNode)

        // Cresate the new document
        String pointer = importTask.createOrUpdateDocumentFromVariantContentNode(
            newContentNode,
            primaryTypeName,
            nodeFullPath,
            locale,
            localizedName)
        log.debug "Create: ${pointer}"

        return true
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }

    void destroy() {
        importTask.stop()
        importTask.logSummary()
        exportTask.stop()
        exportTask.logSummary()
    }
}
