package uk.nhs.digital.nil.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:methodology")
@Node(jcrType = "nationalindicatorlibrary:methodology")
public class Methodology extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:calculation")
    public HippoHtml getCalculation() {
        return getHippoHtml("nationalindicatorlibrary:calculation");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:dataSource")
    public HippoHtml getDataSource() {
        return getHippoHtml("nationalindicatorlibrary:dataSource");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:denominator")
    public HippoHtml getDenominator() {
        return getHippoHtml("nationalindicatorlibrary:denominator");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:numerator")
    public HippoHtml getNumerator() {
        return getHippoHtml("nationalindicatorlibrary:numerator");
    }
}
