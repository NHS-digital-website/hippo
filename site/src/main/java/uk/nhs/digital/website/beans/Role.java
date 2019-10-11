package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:role")
@Node(jcrType = "website:role")
public class Role extends HippoCompound {

    public String getSectionType() {
        return "role";
    }

    @HippoEssentialsGenerated(internalName = "website:primaryrolepicker")
    public HippoBean getPrimaryrolepicker() {
        return getLinkedBean("website:primaryrolepicker", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:primaryrole", allowModifications = false)
    public String getPrimaryrole() {
        JobRole primaryRolePicker = (JobRole)this.getPrimaryrolepicker();
        if (primaryRolePicker != null) {
            return primaryRolePicker.getTitle();
        }
        return getProperty("website:primaryrole");
    }

    @HippoEssentialsGenerated(internalName = "website:primaryroleorgpicker")
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

    @HippoEssentialsGenerated(internalName = "website:boardmembership")
    public HippoHtml getBoardmembership() {
        return getHippoHtml("website:boardmembership");
    }

    @HippoEssentialsGenerated(internalName = "website:contactdetails")
    public ContactDetail getContactdetails() {
        return getBean("website:contactdetails", ContactDetail.class);
    }
}
