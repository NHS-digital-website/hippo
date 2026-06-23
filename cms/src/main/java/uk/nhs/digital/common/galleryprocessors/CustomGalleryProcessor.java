package uk.nhs.digital.common.galleryprocessors;

import static org.hippoecm.repository.api.HippoNodeType.HIPPO_TEXT;

import org.hippoecm.frontend.plugins.gallery.model.GalleryException;
import org.hippoecm.frontend.plugins.gallery.model.NullGalleryProcessor;

import java.io.InputStream;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class CustomGalleryProcessor extends NullGalleryProcessor {

    @Override
    public void makeImage(final Node node, final InputStream istream, final String mimeType, final String fileName) throws GalleryException, RepositoryException {
        super.makeImage(node, istream, mimeType, fileName);

        if (node.hasNode("hippogallery:asset")) {
            final Node asset = node.getNode("hippogallery:asset");
            //WSP-770 set the extracted text to blank
            asset.setProperty(HIPPO_TEXT, "");
        }
    }
}
