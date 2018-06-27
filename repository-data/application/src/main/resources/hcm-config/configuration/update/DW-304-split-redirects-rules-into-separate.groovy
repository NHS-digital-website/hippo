import org.onehippo.repository.update.BaseNodeUpdateVisitor
import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session

class RestructureUrlRewriteDocuments extends BaseNodeUpdateVisitor {

    private final String contentRoot = "/content/urlrewriter/rules/content"
    private final String digitalRoot = "/content/urlrewriter/rules/digital"
    private Session session

    @Override
    void initialize(final Session session) throws RepositoryException {

        super.initialize(session)
        this.session = session

        final char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        final String[] properties = ["new-urlrewriter-folder", "new-urlrewriter-document"]
        def contentFolder = session.getNode(contentRoot)
        def digitalFolder = session.getNode(digitalRoot)
        alphabet.each {
            def name = String.valueOf(it)
            if (!contentFolder.hasNode(name)) {
                def node = contentFolder.addNode(name, "urlrewriter:ruleset")
                node.addMixin("mix:referenceable")
                node.setProperty("hippostd:foldertype", properties)
                session.save()
            }
            if (!digitalFolder.hasNode(name)) {

                def node = digitalFolder.addNode(name, "urlrewriter:ruleset")
                node.addMixin("mix:referenceable")
                node.setProperty("hippostd:foldertype", properties)
                session.save()
            }
        }
    }

    boolean doUpdate(Node node) {
        def parentFolder = node.parent.getPath()
        if (digitalRoot.indexOf(parentFolder) == -1 && contentRoot.indexOf(parentFolder) == -1) {
            return false;
        }

        if (parentFolder == contentRoot) {
            log.debug "Moving conent node ${node.path}"
            def contentTarget = contentRoot + "/" + String.valueOf(node.getName().charAt(0)) + "/" + node.getName()
            session.move(node.getPath(), contentTarget)
            return true

        } else if (parentFolder == digitalRoot) {
            def digitalTarget = digitalRoot + "/" + String.valueOf(node.getName().charAt(0)) + "/" + node.getName()
            log.debug "Moving digital node ${node.path}"
            session.move(node.getPath(), digitalTarget)
            return true

        }

        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }

}
