package uk.nhs.digital.svg;

import org.apache.commons.io.IOUtils;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.servlet.utils.ResourceUtils;
import org.onehippo.repository.util.JcrConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class SvgProvider {

    private static final String IMAGE_TYPE_ORIGINAL = "hippogallery:original";
    private static final Logger LOG = LoggerFactory.getLogger(SvgProvider.class);

    public static String getSvgXmlFromBean(HippoBean svgBean) {

        HippoBean imageTypeSvgBean = svgBean.getBean(IMAGE_TYPE_ORIGINAL);
        try {
            if (hasBinaryData(imageTypeSvgBean.getNode())) {
                LOG.debug("Fetching binary data from {}.", imageTypeSvgBean.getParentBean().getParentBean().getName());
                InputStream svgBinaryStream = imageTypeSvgBean.getNode().getProperty(JcrConstants.JCR_DATA).getBinary().getStream();
                return IOUtils.toString(svgBinaryStream, StandardCharsets.UTF_8.name());
            } else {
                LOG.warn("Could not find Binary data from the SVG section of the document.");
                return null;
            }
        } catch (RepositoryException | IOException exception) {
            LOG.error("Unknown exception occurred while trying to retrieve SVG from repository.", exception);
            return null;
        }
    }

    private static boolean hasBinaryData(Node svgNode) {
        return ResourceUtils.hasBinaryProperty(svgNode, JcrConstants.JCR_DATA);
    }
}