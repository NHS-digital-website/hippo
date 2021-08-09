package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:highlightsection")
@Node(jcrType = "website:highlightsection")
public class HighlightSection extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:highlightstitle")
    public String getHightlightsTitle() {
        return getSingleProperty("website:highlightstitle");
    }

    @HippoEssentialsGenerated(internalName = "website:section")
    public List<HippoBean> getHightlightSection() {
        return getChildBeansByName("website:section");
    }

}
