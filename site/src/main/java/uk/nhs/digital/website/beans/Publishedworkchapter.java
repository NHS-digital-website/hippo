package uk.nhs.digital.website.beans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import uk.nhs.digital.website.beans.Friendlyurls;
import java.util.List;
import uk.nhs.digital.website.beans.Section;

@HippoEssentialsGenerated(internalName = "website:publishedworkchapter")
@Node(jcrType = "website:publishedworkchapter")
public class Publishedworkchapter extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "website:seosummary")
    public String getSeosummary() {
        return getProperty("website:seosummary");
    }

    @HippoEssentialsGenerated(internalName = "website:shortsummary")
    public String getShortsummary() {
        return getProperty("website:shortsummary");
    }

    @HippoEssentialsGenerated(internalName = "website:summary")
    public String getSummary() {
        return getProperty("website:summary");
    }

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "hippotaxonomy:keys")
    public String[] getKeys() {
        return getProperty("hippotaxonomy:keys");
    }

    @HippoEssentialsGenerated(internalName = "website:friendlyurls")
    public Friendlyurls getFriendlyurls() {
        return getBean("website:friendlyurls", Friendlyurls.class);
    }

    @HippoEssentialsGenerated(internalName = "website:sections")
    public List<Section> getSections() {
        return getChildBeansByName("website:sections", Section.class);
    }
}
