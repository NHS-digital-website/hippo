package uk.nhs.digital.common.processor.asset.svgstyle;

import static org.hippoecm.repository.api.HippoNodeType.HIPPO_TEXT;

import org.hippoecm.frontend.plugins.gallery.model.GalleryException;
import org.hippoecm.frontend.plugins.gallery.model.NullGalleryProcessor;

import java.io.InputStream;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class SvgStyleGalleryProcessor extends NullGalleryProcessor {

    public static final String DEFAULT_SVG_ASSET_STYLESHEET_URL = "svg.asset.stylesheet.url";

    @Override
    public void makeImage(final Node node, final InputStream istream, final String mimeType, final String fileName) throws GalleryException, RepositoryException {
        super.makeImage(node, istream, mimeType, fileName);

        if (node.hasNode("hippogallery:asset")) {
            final Node asset = node.getNode("hippogallery:asset");
            if (asset.hasProperty(HIPPO_TEXT)) {
                asset.setProperty(HIPPO_TEXT, "");
            }
        }
    }
}
