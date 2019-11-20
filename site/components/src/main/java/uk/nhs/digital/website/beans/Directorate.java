package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:directorate")
@Node(jcrType = "website:directorate")
public class Directorate extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:mainbusinessunit")
    public ComponentBusinessUnit  getMainbusinessunit() {
        return getBean("website:mainbusinessunit", ComponentBusinessUnit.class);
    }

    @HippoEssentialsGenerated(internalName = "website:backgroundcolor")
    public String getBackgroundcolor() {
        return getProperty("website:backgroundcolor");
    }

    @HippoEssentialsGenerated(internalName = "website:embedbackgroundcolor")
    public String getEmbedbackgroundcolor() {
        return getProperty("website:embedbackgroundcolor");
    }

    @HippoEssentialsGenerated(internalName = "website:fontcolor")
    public String getFontcolor() {
        return getProperty("website:fontcolor");
    }

    @HippoEssentialsGenerated(internalName = "website:componentbusinessunits")
    public List<HippoBean> getComponentbusinessunits() {
        return getChildBeansByName("website:componentbusinessunits");
    }

}
