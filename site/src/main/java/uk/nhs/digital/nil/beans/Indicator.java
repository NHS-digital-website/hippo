package uk.nhs.digital.nil.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.ps.beans.BaseDocument;

import java.util.Calendar;

@HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:indicator")
@Node(jcrType = "nationalindicatorlibrary:indicator")
public class Indicator extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:definition")
    public String getDefinition() {
        return getProperty("nationalindicatorlibrary:definition");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:title")
    public String getTitle() {
        return getProperty("nationalindicatorlibrary:title");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:basedOn")
    public String getBasedOn() {
        return getProperty("nationalindicatorlibrary:basedOn");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:reportingLevel")
    public String getReportingLevel() {
        return getProperty("nationalindicatorlibrary:reportingLevel");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:rating")
    public String getRating() {
        return getProperty("nationalindicatorlibrary:rating");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:indicatorSet")
    public String getIndicatorSet() {
        return getProperty("nationalindicatorlibrary:indicatorSet");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:interpretationGuidelines")
    public String getInterpretationGuidelines() {
        return getProperty("nationalindicatorlibrary:interpretationGuidelines");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:caveats")
    public String getCaveats() {
        return getProperty("nationalindicatorlibrary:caveats");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:assuranceDate")
    public Calendar getAssuranceDate() {
        return getProperty("nationalindicatorlibrary:assuranceDate");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:contactAuthor")
    public String getContactAuthor() {
        return getProperty("nationalindicatorlibrary:contactAuthor");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:iapCode")
    public String getIapCode() {
        return getProperty("nationalindicatorlibrary:iapCode");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:publishedBy")
    public String getPublishedBy() {
        return getProperty("nationalindicatorlibrary:publishedBy");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:publishedDate")
    public Calendar getPublishedDate() {
        return getProperty("nationalindicatorlibrary:publishedDate");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:purpose")
    public String getPurpose() {
        return getProperty("nationalindicatorlibrary:purpose");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:reportingPeriod")
    public String getReportingPeriod() {
        return getProperty("nationalindicatorlibrary:reportingPeriod");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:reviewDate")
    public Calendar getReviewDate() {
        return getProperty("nationalindicatorlibrary:reviewDate");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:descriptor")
    public String getDescriptor() {
        return getProperty("nationalindicatorlibrary:descriptor");
    }
}
