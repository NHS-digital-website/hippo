package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:group")
@Node(jcrType = "website:group")
public class Group extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:quorate")
    public Long getQuorate() {
        return getSingleProperty("website:quorate");
    }

    @HippoEssentialsGenerated(internalName = "website:about")
    public HippoHtml getAbout() {
        return getHippoHtml("website:about");
    }

    @HippoEssentialsGenerated(internalName = "website:roles")
    public List<HippoBean> getRoles() {
        return getChildBeansByName("website:roles");
    }

    @HippoEssentialsGenerated(internalName = "website:sections")
    public List<HippoBean> getSections() {
        return getChildBeansByName("website:sections");
    }

    @HippoEssentialsGenerated(internalName = "website:groupreports")
    public HippoBean getGroupreports() {
        return getLinkedBean("website:groupreports", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "hippotaxonomy:topics")
    public String[] getTopics() {
        return getMultipleProperty("hippotaxonomy:topics");
    }

}
