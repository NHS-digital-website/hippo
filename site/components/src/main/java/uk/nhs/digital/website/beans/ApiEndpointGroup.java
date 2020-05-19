package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:apiendpointgroup")
@Node(jcrType = "website:apiendpointgroup")
public class ApiEndpointGroup extends HippoCompound {

    public String getSectionType() {
        return "apiendpointgroup";
    }

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getSingleProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:apirequest")
    public List<HippoBean> getApirequest() {
        return getLinkedBeans("website:apirequest", HippoBean.class);
    }

}
