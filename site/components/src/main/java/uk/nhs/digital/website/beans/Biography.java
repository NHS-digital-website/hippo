package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:biography")
@Node(jcrType = "website:biography")
public class Biography extends BaseCompound {

    public String getSectionType() {
        return "biography";
    }


    @Override
    public String getTitle() {
        return null;
    }
}

