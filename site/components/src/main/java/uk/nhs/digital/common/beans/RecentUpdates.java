package uk.nhs.digital.common.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:recentupdates")
@Node(jcrType = "website:recentupdates")
public class RecentUpdates extends BaseDocument {

    @HippoEssentialsGenerated(internalName = "website:pageTitle")
    public String getPageTitle() {
        return getSingleProperty("website:pageTitle");
    }

    @HippoEssentialsGenerated(internalName = "website:itemsPerPage")
    public long getItemsPerPage() {
        return getSingleProperty("website:itemsPerPage");
    }

    @HippoEssentialsGenerated(internalName = "website:pickerPath")
    public String getPickerPath() {
        return getSingleProperty("website:pickerPath");
    }
}
