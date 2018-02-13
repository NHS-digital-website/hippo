package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.*;
import org.hippoecm.hst.content.beans.standard.*;
import org.onehippo.cms7.essentials.dashboard.annotations.*;

@HippoEssentialsGenerated(internalName = "website:calltoaction")
@Node(jcrType = "website:calltoaction")
public class Calltoaction extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "website:Label")
    public String getLabel() {
        return getProperty("website:Label");
    }

    @HippoEssentialsGenerated(internalName = "website:external")
    public String getExternal() {
        return getProperty("website:external");
    }

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:internal")
    public HippoBean getInternal() {
        return getLinkedBean("website:internal", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:content")
    public String getContent() {
        return getProperty("website:content");
    }

    @HippoEssentialsGenerated(internalName = "website:image")
    public HippoGalleryImageSet getImage() {
        return getLinkedBean("website:image", HippoGalleryImageSet.class);
    }
}
