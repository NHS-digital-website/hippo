package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:otherrole")
@Node(jcrType = "website:otherrole")
public class OtherRole extends HippoCompound {

    public String getSectionType() {
        return "otherrole";
    }

    @HippoEssentialsGenerated(internalName = "website:secondaryrole")
    public String gerSecondaryrole() {
        return getSingleProperty("website:secondaryrole");
    }

    @HippoEssentialsGenerated(internalName = "website:secondaryroleorg")
    public String gerSecondaryroleorg() {
        return getSingleProperty("website:secondaryroleorg");
    }
}
