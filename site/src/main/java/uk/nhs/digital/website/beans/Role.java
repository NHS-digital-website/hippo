package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:role")
@Node(jcrType = "website:role")
public class Role extends HippoCompound {

    public String getSectionType() {
        return "role";
    }

    @HippoEssentialsGenerated(internalName = "website:primaryrole")
    public String gerPrimaryrole() {
        return getProperty("website:primaryrole");
    }

    @HippoEssentialsGenerated(internalName = "website:primaryroleorg")
    public String gerPrimaryroleorg() {
        return getProperty("website:primaryroleorg");
    }

    @HippoEssentialsGenerated(internalName = "website:otherroles")
    public List<HippoBean> getOtherroles() {
        return getChildBeansByName("website:otherroles");
    }

    @HippoEssentialsGenerated(internalName = "website:boardmembership")
    public HippoHtml getBoardmembership() {
        return getHippoHtml("website:boardmembership");
    }

    @HippoEssentialsGenerated(internalName = "website:contactdetails")
    public ContactDetail getContactdetailes() {
        return getBean("website:contactdetails", ContactDetail.class);
    }
}
