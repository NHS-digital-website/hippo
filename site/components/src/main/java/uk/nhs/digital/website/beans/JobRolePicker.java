package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:jobrolepicker")
@Node(jcrType = "website:jobrolepicker")
public class JobRolePicker extends HippoCompound {


    @HippoEssentialsGenerated(internalName = "website:primaryrolepicker", allowModifications = false)
    public JobRole getPrimaryrolepicker() {
        return getLinkedBean("website:primaryrolepicker", JobRole.class);
    }

    @HippoEssentialsGenerated(internalName = "website:selection", allowModifications = false)
    public String getSelection() {
        return getProperty("website:selection");
    }

}
