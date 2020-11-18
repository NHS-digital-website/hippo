package eximgroovy

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.lang.text.StrSubstitutor
import org.hippoecm.repository.api.HippoWorkspace
import org.hippoecm.repository.api.WorkflowManager
import org.hippoecm.repository.impl.PropertyDecorator
import org.hippoecm.repository.util.JcrUtils
import org.onehippo.forge.content.exim.core.DocumentManager
import org.onehippo.forge.content.exim.core.impl.WorkflowDocumentManagerImpl
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.PropertyIterator
import javax.jcr.Session
import javax.jcr.query.Query
import javax.jcr.query.QueryManager
import javax.jcr.query.QueryResult

class MoveNewsYearWise extends BaseNodeUpdateVisitor {
    public static final String CONTENT_ROOT = "/content/documents/corporate-website/"
    WorkflowManager workflowManager
    DocumentManager documentManager
    String baseDestinationPath
    String moveDocFromPath
    String baseURL
    Session session
    CSVPrinter printer = new CSVPrinter(new FileWriter("news.csv"), CSVFormat.DEFAULT)
    QueryManager manager
    QueryResult result
    Node ruleFolderNode
    Node newsFolderNode
    public static final String AVAILABILITY = "live"
    def availability = [AVAILABILITY] as String[]
    String ruleFolder

    void initialize(Session session) {
        this.session = session
        workflowManager = ((HippoWorkspace) session.getWorkspace()).getWorkflowManager()
        documentManager = new WorkflowDocumentManagerImpl(session)

        baseDestinationPath = StrSubstitutor.replaceSystemProperties(parametersMap.get("baseDestinationPath"))
        moveDocFromPath = StrSubstitutor.replaceSystemProperties(parametersMap.get("moveDocFromPath"))
        baseURL = StrSubstitutor.replaceSystemProperties(parametersMap.get("baseURL"))
        ruleFolder = StrSubstitutor.replaceSystemProperties(parametersMap.get("ruleFolder"))

        printer.printRecord("Year of Pub", "Current URL", "Desired URL");
        manager = session.getWorkspace().getQueryManager();
        result = manager.createQuery("/jcr:root/content/urlrewriter/rules", Query.XPATH).execute()
        def folderNode = result.getNodes().iterator().next()
        ruleFolderNode = createFolder(folderNode, ruleFolder, "urlrewriter:ruleset")
        result = manager.createQuery("/jcr:root/content/documents/corporate-website/news-and-events", Query.XPATH).execute()
        newsFolderNode = result.getNodes().next()

    }

    boolean doUpdate(Node node) {


        final parentPath = node.getParent().getPath()

        PropertyIterator iter1 = node.properties;
        while (iter1.hasNext()) {
            PropertyDecorator pd = iter1.nextProperty();
            if ("hippostdpubwf:publicationDate".equalsIgnoreCase(pd.name)) {
                JcrUtils.ensureIsCheckedOut(node)

                String publicationYear = pd.getDate().get(Calendar.YEAR)

                if (!parentPath.contains("/" + publicationYear + "/")
                    && (parentPath.equalsIgnoreCase(baseDestinationPath + "/" + node.name)
                    || parentPath.equalsIgnoreCase(moveDocFromPath + "/" + node.name))
                ) {
                    final String oldParentFolderPath = parentPath
                    String targetFolderPath = baseDestinationPath.concat("/" + publicationYear + "/").concat(node.name)
                    log.info("Year of Pub." + publicationYear + " Current URL " + oldParentFolderPath + " Desired URL  " + targetFolderPath);
                    log.debug("Year of Pub." + publicationYear + " Current URL " + oldParentFolderPath + " Desired URL  " + targetFolderPath);
                    createFolder(newsFolderNode, publicationYear, "hippostd:folder");
                    session.move(oldParentFolderPath, targetFolderPath);
                    def oldURL = oldParentFolderPath.replace(CONTENT_ROOT, "/");
                    def newURL = targetFolderPath.replace(CONTENT_ROOT, baseURL);
                    log.debug("Year of Pub." + publicationYear + " Current URL " + oldURL + " Desired URL  " + newURL);
                    createRule(ruleFolderNode, node.name, oldURL, newURL)
                    printer.printRecord(publicationYear, oldURL, newURL);
                }

                return true
            }
        }
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }

    void createRule(final Node node, final String nodeName, String oldURL, String newURL) {
        if (node.hasNode(nodeName)) {
            log.debug(" Node already EXISTS " + nodeName)
            return
        }
        JcrUtils.ensureIsCheckedOut(node)
        log.debug("  Creating Document rule")
        Node handle = node.addNode(nodeName, "hippo:handle")
        handle.addMixin("hippo:named")
        handle.setProperty("hippo:name", nodeName)
        handle.addMixin("mix:referenceable")
        Node document = handle.addNode(nodeName, "urlrewriter:rule")

        document.setProperty("urlrewriter:rulefrom", oldURL)
        document.setProperty("urlrewriter:ruleto", newURL)

        document.setProperty("hippostdpubwf:lastModifiedBy", "admin")
        document.setProperty("hippostd:stateSummary", "live")
        document.setProperty("urlrewriter:ruletype", "permanent-redirect")
        document.setProperty("hippo:availability", availability)
        document.setProperty("urlrewriter:ruledescription", "Rule for " + nodeName)
        document.setProperty("hippotranslation:id", UUID.randomUUID().toString())
        document.setProperty("hippostd:state", "published")
        document.setProperty("hippostdpubwf:publicationDate", Calendar.getInstance())
        document.setProperty("hippostdpubwf:lastModificationDate", Calendar.getInstance())
        document.setProperty("hippostdpubwf:lastModificationDate", Calendar.getInstance())
        document.setProperty("hippostdpubwf:createdBy", "admin")
        document.setProperty("hippostdpubwf:creationDate", Calendar.getInstance())

        log.debug("  Rule Created ")

        document.getSession().save()
    }

    private Node createFolder(Node node, String folderName, String folderType) {
        log.debug("Creating Folder " + folderName)
        if (!node.hasNode(folderName)) {
            JcrUtils.ensureIsCheckedOut(node)
            Node folder = node.addNode(folderName, folderType)
            folder.getSession().save()
            return folder
        }
        return node.getNode(folderName)
    }

    void destroy() {
        printer.close()
    }
}
