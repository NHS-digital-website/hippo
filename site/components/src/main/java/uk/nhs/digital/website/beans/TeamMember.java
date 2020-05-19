package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:teammember")
@Node(jcrType = "website:teammember")
public class TeamMember extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:roleinteam")
    public String getRoleInTeam() {
        return getSingleProperty("website:roleinteam");
    }

    @HippoEssentialsGenerated(internalName = "website:person")
    public HippoBean getPerson() {
        return getLinkedBean("website:person", HippoBean.class);
    }

}
