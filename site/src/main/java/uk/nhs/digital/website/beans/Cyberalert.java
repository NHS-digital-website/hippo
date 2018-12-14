package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Calendar;
import java.util.List;

@HippoEssentialsGenerated(internalName = "website:cyberalert")
@Node(jcrType = "website:cyberalert")
public class Cyberalert extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:publisheddate")
    public Calendar getPublisheddate() {
        return getProperty("website:publisheddate");
    }

    @HippoEssentialsGenerated(internalName = "website:address")
    public List<Address> getAddress() {
        return getChildBeansByName("website:address", Address.class);
    }
}
