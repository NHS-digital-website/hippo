package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:tilesectionvisualhub")
@Node(jcrType = "website:tilesectionvisualhub")
public class TileSectionVisualHub extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:tilesectionheading")
    public String getTileSectionHeading() {
        return getProperty("website:tilesectionheading");
    }

    @HippoEssentialsGenerated(internalName = "website:tilesectionintroduction")
    public HippoHtml getTileSectionIntroduction() {
        return getHippoHtml("website:tilesectionintroduction");
    }

    @HippoEssentialsGenerated(internalName = "website:tilesectionlinks")
    public List<HippoBean> getTileSectionLinks() {
        return getChildBeansByName("website:tilesectionlinks");
    }

}
