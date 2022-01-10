package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoAsset;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:customizedAssetSet")
@Node(jcrType = "website:customizedAssetSet")
public class CustomizedAssetSet extends HippoAsset {

    @HippoEssentialsGenerated(internalName = "website:meetpdfa")
    public Boolean getMeetpdfa() {
        return getSingleProperty("website:meetpdfa");
    }

    @HippoEssentialsGenerated(internalName = "website:archiveMaterial")
    public Boolean getArchiveMaterial() {
        return getSingleProperty("website:archiveMaterial");
    }

}
