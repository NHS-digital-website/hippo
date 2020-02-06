package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:team")
@Node(jcrType = "website:team")
public class Team extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:sections")
    public List<HippoBean> getSections() {
        return getChildBeansByName("website:sections");
    }

    @HippoEssentialsGenerated(internalName = "website:responsiblepeople")
    public List<HippoBean> getResponsiblePeople() {
        return getLinkedBeans("website:responsiblepeople", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:teammembers")
    public List<HippoBean> getTeamMembers() {
        return getChildBeansByName("website:teammembers");
    }

    @HippoEssentialsGenerated(internalName = "website:contactdetails", allowModifications = false)
    public ContactDetailExtended getContactdetails() {
        return getBean("website:contactdetails", ContactDetailExtended.class);
    }
}
