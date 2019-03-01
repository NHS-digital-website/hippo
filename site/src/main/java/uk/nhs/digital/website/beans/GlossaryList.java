package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:glossarylist")
@Node(jcrType = "website:glossarylist")
public class GlossaryList extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:indexpage")
    public Boolean getIndexPage() {
        return getProperty("website:indexpage");
    }

    @HippoEssentialsGenerated(internalName = "website:glossaryitems")
    public List<HippoBean> getGlossaryItems() {
        return getChildBeansByName("website:glossaryitems");
    }

}
