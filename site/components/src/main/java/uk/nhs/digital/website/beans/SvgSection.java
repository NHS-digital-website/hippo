package uk.nhs.digital.website.beans;

import org.apache.commons.io.IOUtils;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.servlet.utils.ResourceUtils;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.onehippo.repository.util.JcrConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.jcr.RepositoryException;


@HippoEssentialsGenerated(internalName = "website:svg")
@Node(jcrType = "website:svg")
public class SvgSection extends HippoCompound {

    private static final String IMAGE_TYPE_ORIGINAL = "hippogallery:original";
    private static final Logger LOG = LoggerFactory.getLogger(SvgSection.class);

    public String getSectionType() {
        return "svg";
    }

    @HippoEssentialsGenerated(internalName = "website:altText")
    public String getAltText() {
        return getSingleProperty("website:altText");
    }

    @HippoEssentialsGenerated(internalName = "website:link")
    public CorporateWebsiteImageset getLink() {
        return getLinkedBean("website:link", CorporateWebsiteImageset.class);
    }

    public String getSvgXmlFromRepository() throws RepositoryException, IOException {
        HippoBean imageBean = getLinkedBean("website:link", CorporateWebsiteImageset.class);
        HippoBean imageTypeBean = imageBean.getBean(IMAGE_TYPE_ORIGINAL);
        boolean hasBinaryData = ResourceUtils.hasBinaryProperty(imageTypeBean.getNode(),JcrConstants.JCR_DATA);

        if (hasBinaryData) {
            LOG.debug("Fetching binary data from {}.", this.getParentBean().getParentBean().getName());
            InputStream svgBinaryStream = imageTypeBean.getNode().getProperty(JcrConstants.JCR_DATA).getBinary().getStream();
            return IOUtils.toString(svgBinaryStream, StandardCharsets.UTF_8.name());
        } else {
            LOG.warn("Could not find Binary data from the SVG section of the document.");
            return null;
        }
    }

}