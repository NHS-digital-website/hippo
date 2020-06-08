package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.*;
import org.hippoecm.hst.content.beans.standard.*;
import org.onehippo.cms7.essentials.dashboard.annotations.*;

@HippoEssentialsGenerated(internalName = "website:calltoaction")
@Node(jcrType = "website:calltoaction")
public class Calltoaction extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "website:Label")
    public String getLabel() {
        return getSingleProperty("website:Label");
    }

    @HippoEssentialsGenerated(internalName = "website:external")
    public String getExternal() {
        return getSingleProperty("website:external");
    }

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getSingleProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:internal")
    public HippoBean getInternal() {
        return getLinkedBean("website:internal", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:content")
    public String getContent() {
        return getSingleProperty("website:content");
    }

    @HippoEssentialsGenerated(internalName = "website:image")
    public CorporateWebsiteImageset getImage() {
        return getLinkedBean("website:image", CorporateWebsiteImageset.class);
    }
}
