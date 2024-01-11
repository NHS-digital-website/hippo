package uk.nhs.digital.common.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:recentupdates")
@Node(jcrType = "website:recentupdates")
public class RecentUpdates extends BaseDocument {

    @HippoEssentialsGenerated(internalName = "website:itemsPerPage")
    public long getItemsPerPage() {
        return getSingleProperty("website:itemsPerPage");
    }

    @HippoEssentialsGenerated(internalName = "website:pickerPath")
    public String getPickerPath() {
        return getSingleProperty("website:pickerPath");
    }

    @HippoEssentialsGenerated(internalName = "website:pickerType")
    public String getPickerType() {
        return getSingleProperty("website:pickerType");
    }

    @HippoEssentialsGenerated(internalName = "website:includeChildren")
    public boolean getIncludeChildren() {
        return getSingleProperty("website:includeChildren");
    }
}
