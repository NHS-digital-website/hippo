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

    @HippoEssentialsGenerated(internalName = "website:rolepicker")
    public List<HippoBean> getRolepicker() {
        return getChildBeansByName("website:rolepicker");
    }

    @HippoEssentialsGenerated(internalName = "website:primaryrole", allowModifications = false)
    public List<String> getPrimaryroles() {
        List<String> roles = new ArrayList<String>();

        List<HippoBean> rolepickers = this.getRolepicker();
        for (HippoBean picker : rolepickers) {
            JobRolePicker rolepicker = (JobRolePicker)picker;
            if (rolepicker != null && rolepicker.getPrimaryrolepicker() != null) {
                JobRole jobrole = rolepicker.getPrimaryrolepicker();
                if (jobrole != null) {
                    roles.add(jobrole.getTitle());
                }
            }
        }
        if (roles.size() == 0) {
            roles.add(getSingleProperty("website:primaryrole"));
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
        return getSingleProperty("website:primaryroleorg");
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
