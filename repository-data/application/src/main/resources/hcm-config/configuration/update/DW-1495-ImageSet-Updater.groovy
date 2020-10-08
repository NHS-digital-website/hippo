package uk.nhs.digital.updater


import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.Session
import javax.jcr.query.Query

/**
 * Before updating image sets, run this script for image set you need to convert
 * //content//element(*, hippogallery:stdImageGallery)
 */
class PrepareImageSetUpdater extends BaseNodeUpdateVisitor {

    private static final String HIPPOSTD_FOLDERTYPE = "hippostd:foldertype"
    private static final String HIPPOSTD_GALLERYTYPE = "hippostd:gallerytype"
    public static final String NEW_FOLDER = "new-website-CorporateImageSetSpecification-folder"
    public static final String NEW_IMAGE = "website:CorporateImageSetSpecification"
    public static final String OLD_TYPE = "hippogallery:imageset"
    // only folders of OLD_TYPE will be processed
    private boolean exactMatch = true

    private Session session

    void initialize(Session session) throws RepositoryException {

        this.session = session
        log.info "Initialized script ${this.getClass().getName()}"
    }

    boolean doUpdate(Node node) {
        try {
            prepareExistingImageSet(session, node)
            return true
        } catch (RepositoryException e) {
            log.error("Exception while converting  image set variants", e)
            node.getSession().refresh(false/*keepChanges*/)
        }
        return false
    }

    @Override
    boolean undoUpdate(final Node node) throws RepositoryException, UnsupportedOperationException {
        return false
    }

    void prepareExistingImageSet(Session session, Node imageFolderNode) {
        if (exactMatch) {
            if (!imageFolderNode.hasProperty("hippostd:gallerytype")
                || imageFolderNode.getProperty("hippostd:gallerytype").values.size() == 0
            ) {
                log.debug("Missing hippostd:gallerytype property, skipping: {}", imageFolderNode.path)
                return
            }
            if (imageFolderNode.getProperty("hippostd:gallerytype").values[0].string != OLD_TYPE) {
                log.debug("Skipping, not same as OLD_TYPE: {}", imageFolderNode.path)
                return
            }
        }
        log.info("@@@ processing: {}", imageFolderNode.path)
        String[] folderTypes = [NEW_FOLDER]
        String[] imgTypes = [NEW_IMAGE]
        imageFolderNode.setProperty(HIPPOSTD_FOLDERTYPE, folderTypes)
        imageFolderNode.setProperty(HIPPOSTD_GALLERYTYPE, imgTypes)
        // change primary types:
        final Query handleQuery = session.getWorkspace().getQueryManager().createQuery("//content/gallery//element(*, hippo:handle)", "xpath")
        final NodeIterator handleNodes = handleQuery.execute().getNodes()
        while (handleNodes.hasNext()) {
            final Node handle = handleNodes.nextNode()
            final String name = handle.getName()
            if (handle.hasNode(name)) {
                final Node myImageNode = handle.getNode(name)
                myImageNode.setPrimaryType(NEW_IMAGE)
            } else {
                log.warn("Invalid handle {}", handle.getPath())
            }
        }
    }
}
