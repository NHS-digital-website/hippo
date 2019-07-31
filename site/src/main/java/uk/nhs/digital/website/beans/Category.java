package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;


@HippoEssentialsGenerated(internalName = "website:category")
@Node(jcrType = "website:category")
public class Category extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:name")
    public String getName() {
        return getProperty("website:name");
    }

    @HippoEssentialsGenerated(internalName = "website:description")
    public HippoHtml getDescription() {
        return getHippoHtml("website:description");
    }

    @HippoEssentialsGenerated(internalName = "website:linkservices")
    public List<HippoBean> getLinkservices() {
        return getLinkedBeans("website:linkservices", HippoBean.class);
    }

    public String getSectionType() {
        return "category";
    }
}
