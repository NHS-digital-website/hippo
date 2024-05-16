package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:categories")
@Node(jcrType = "website:categories")
public class Categories extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:categoryName")
    public String getName() {
        return getSingleProperty("website:categoryName");
    }

    @HippoEssentialsGenerated(internalName = "website:description")
    public HippoHtml getDescription() {
        return getHippoHtml("website:description");
    }

    @HippoEssentialsGenerated(internalName = "website:linkedService")
    public List<HippoBean> getLinkservices() {
        return getLinkedBeans("website:linkedService", HippoBean.class);
    }

    public String getSectionType() {
        return "categories";
    }
}
