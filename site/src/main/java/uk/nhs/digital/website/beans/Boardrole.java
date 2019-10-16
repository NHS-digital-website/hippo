package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:boardrole")
@Node(jcrType = "website:boardrole")
public class Boardrole extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:boardposition")
    public String getBoardposition() {
        return getProperty("website:boardposition");
    }

    @HippoEssentialsGenerated(internalName = "website:role")
    public HippoBean getRole() {
        return getLinkedBean("website:role", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:responsibilities")
    public HippoHtml getResponsibilities() {
        return getHippoHtml("website:responsibilities");
    }
}
