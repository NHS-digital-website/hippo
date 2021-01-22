package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:visualhub")
@Node(jcrType = "website:visualhub")
public class VisualHub extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:image")
    public CorporateWebsiteImageset getImage() {
        return getLinkedBean("website:image", CorporateWebsiteImageset.class);
    }

    public CorporateWebsiteImageset getPageIcon() {
        return this.getIcon();
    }

    @HippoEssentialsGenerated(internalName = "website:icon")
    public CorporateWebsiteImageset getIcon() {
        return getLinkedBean("website:icon", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:primarysectionvisualhub")
    public List<HippoBean> getPrimarySections() {
        return getChildBeansByName("website:primarysectionvisualhub");
    }

    @HippoEssentialsGenerated(internalName = "website:tilesectionvisualhub")
    public List<HippoBean> getTileSections() {
        return getChildBeansByName("website:tilesectionvisualhub");
    }

    @HippoEssentialsGenerated(internalName = "website:additionalinformation")
    public HippoHtml getAdditionalInformation() {
        return getHippoHtml("website:additionalinformation");
    }

    @HippoEssentialsGenerated(internalName = "website:introduction")
    public HippoHtml getIntroduction() {
        return getHippoHtml("website:introduction");
    }
}
