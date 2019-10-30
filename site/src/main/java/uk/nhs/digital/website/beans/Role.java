package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.ArrayList;
import java.util.List;

@HippoEssentialsGenerated(internalName = "website:role")
@Node(jcrType = "website:role")
public class Role extends HippoCompound {

    public String getSectionType() {
        return "role";
    }

    @HippoEssentialsGenerated(internalName = "website:primaryrolepicker", allowModifications = false)
    public List<HippoBean> getPrimaryrolepicker() {
        return getLinkedBeans("website:primaryrolepicker", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:primaryrole", allowModifications = false)
    public List<String> getPrimaryroles() {
        List<HippoBean> primaryRolePicker = (List<HippoBean>)this.getPrimaryrolepicker();
        List<String> roles = new ArrayList<String>();
        if (primaryRolePicker.size() > 0) {
            for (HippoBean role : primaryRolePicker) {
                JobRole jobrole = (JobRole)role;
                roles.add(jobrole.getTitle());
            }
        } else {
            roles.add(getProperty("website:primaryrole"));
        }
        return roles;
    }

    public String getFirstprimaryrole() {
        List<String> roles = getPrimaryroles();
        if (roles.size() > 0) {
            return roles.get(0);
        }
        return null;
    }

    @HippoEssentialsGenerated(internalName = "website:primaryroleorgpicker", allowModifications = false)
    public HippoBean getPrimaryroleorgpicker() {
        return getLinkedBean("website:primaryroleorgpicker", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:primaryroleorg", allowModifications = false)
    public String getPrimaryroleorg() {
        Organisation primaryJobOrg = (Organisation)this.getPrimaryroleorgpicker();
        if (primaryJobOrg != null) {
            return primaryJobOrg.getTitle();
        }
        return getProperty("website:primaryroleorg");
    }

    @HippoEssentialsGenerated(internalName = "website:boardmembership", allowModifications = false)
    public HippoHtml getBoardmembership() {
        return getHippoHtml("website:boardmembership");
    }

    @HippoEssentialsGenerated(internalName = "website:contactdetails", allowModifications = false)
    public ContactDetail getContactdetails() {
        return getBean("website:contactdetails", ContactDetail.class);
    }
}
