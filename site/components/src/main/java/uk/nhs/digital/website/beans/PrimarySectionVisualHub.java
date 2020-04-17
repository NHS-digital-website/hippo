package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:primarysectionvisualhub")
@Node(jcrType = "website:primarysectionvisualhub")
public class PrimarySectionVisualHub extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:primarysectionheading")
    public String getPrimarySectionHeading() {
        return getProperty("website:primarysectionheading");
    }

    @HippoEssentialsGenerated(internalName = "website:primarytilesectionlinks")
        public List<HippoBean> getPrimarySectionsTiles() {
        return getChildBeansByName("website:primarytilesectionlinks");
    }

}
