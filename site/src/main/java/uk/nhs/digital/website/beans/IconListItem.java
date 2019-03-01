package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:iconlistitem")
@Node(jcrType = "website:iconlistitem")
public class IconListItem extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:image")
    public HippoGalleryImageSet getImage() {
        return getLinkedBean("website:image", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:heading")
    public String getHeading() {
        return getProperty("website:heading");
    }

    @HippoEssentialsGenerated(internalName = "website:description")
    public HippoHtml getDescription() {
        return getHippoHtml("website:description");
    }

}
