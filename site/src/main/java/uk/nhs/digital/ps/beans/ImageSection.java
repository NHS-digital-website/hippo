package uk.nhs.digital.ps.beans;

import static org.apache.cxf.common.util.StringUtils.isEmpty;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoResource;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "publicationsystem:imageSection")
@Node(jcrType = "publicationsystem:imageSection")
public class ImageSection extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "publicationsystem:image")
    public HippoResource getImage() {
        return getChildBeansByName("publicationsystem:image", HippoResource.class).get(0);
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:link")
    public String getLink() {
        return getProperty("publicationsystem:link");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:altText")
    public String getAltText() {
        return getProperty("publicationsystem:altText");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:caption")
    public String getCaption() {
        return getProperty("publicationsystem:caption");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:imageSize")
    private String getImageSize() {
        return getProperty("publicationsystem:imageSize");
    }

    public Size getSize() {
        String imageSize = getImageSize();

        // Temporarily adding "old" option which won't try to resize the images
        return isEmpty(imageSize) || imageSize.equalsIgnoreCase("old")
            ? null
            : Size.valueOf(imageSize.toUpperCase());
    }

    public String getSectionType() {
        return "image";
    }

    public enum Size {
        HALF,
        FULL
    }
}
