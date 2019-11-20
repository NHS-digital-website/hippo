package uk.nhs.digital.nil.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:details")
@Node(jcrType = "nationalindicatorlibrary:details")
public class Details extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:iapCode")
    public String getIapCode() {
        return getProperty("nationalindicatorlibrary:iapCode");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:indicatorSet")
    public String getIndicatorSet() {
        return getProperty("nationalindicatorlibrary:indicatorSet");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:rating")
    public String getRating() {
        return getProperty("nationalindicatorlibrary:rating");
    }

    @HippoEssentialsGenerated(internalName = "hippotaxonomy:keys")
    public String[] getKeys() {
        return getProperty("hippotaxonomy:keys");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:definition")
    public HippoHtml getDefinition() {
        return getHippoHtml("nationalindicatorlibrary:definition");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:methodology")
    public Methodology getMethodology() {
        return getBean("nationalindicatorlibrary:methodology",
                Methodology.class);
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:purpose")
    public HippoHtml getPurpose() {
        return getHippoHtml("nationalindicatorlibrary:purpose");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:caveats")
    public HippoHtml getCaveats() {
        return getHippoHtml("nationalindicatorlibrary:caveats");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:interpretationGuidelines")
    public HippoHtml getInterpretationGuidelines() {
        return getHippoHtml("nationalindicatorlibrary:interpretationGuidelines");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:briefDescription")
    public String getBriefDescription() {
        return getProperty("nationalindicatorlibrary:briefDescription");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:qualityStatementUrl")
    public String getQualityStatementUrl() {
        return getProperty("nationalindicatorlibrary:qualityStatementUrl");
    }  

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:technicalSpecificationUrl")
    public String getTechnicalSpecificationUrl() {
        return getProperty("nationalindicatorlibrary:technicalSpecificationUrl");
    }       
}
