package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:grouprole")
@Node(jcrType = "website:grouprole")
public class Grouprole extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:groupposition")
    public String getGroupposition() {
        return getSingleProperty("website:groupposition");
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
