package uk.nhs.digital.nil.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Calendar;

@HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:topbar")
@Node(jcrType = "nationalindicatorlibrary:topbar")
public class Topbar extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:assuranceDate")
    public Calendar getAssuranceDate() {
        return getProperty("nationalindicatorlibrary:assuranceDate");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:assuredStatus")
    public Boolean getAssuredStatus() {
        return getProperty("nationalindicatorlibrary:assuredStatus");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:publishedBy")
    public String getPublishedBy() {
        return getProperty("nationalindicatorlibrary:publishedBy");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:reviewDate")
    public Calendar getReviewDate() {
        return getProperty("nationalindicatorlibrary:reviewDate");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:title")
    public String getTitle() {
        return getProperty("nationalindicatorlibrary:title");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:basedOn")
    public String getBasedOn() {
        return getProperty("nationalindicatorlibrary:basedOn");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:contactAuthor")
    public ContactAuthor getContactAuthor() {
        return getBean("nationalindicatorlibrary:contactAuthor", ContactAuthor.class);
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:reportingLevel")
    public String getReportingLevel() {
        return getProperty("nationalindicatorlibrary:reportingLevel");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:reportingPeriod")
    public String getReportingPeriod() {
        return getProperty("nationalindicatorlibrary:reportingPeriod");
    }
}
