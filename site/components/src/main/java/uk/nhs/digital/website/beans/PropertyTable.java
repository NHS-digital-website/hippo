package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:propertytable")
@Node(jcrType = "website:propertytable")
public class PropertyTable extends BaseCompound {

    public String getSectionType() {
        return "propertytable";
    }
    
    @Override
    public String getTitle() {
        return null;
    }
}
