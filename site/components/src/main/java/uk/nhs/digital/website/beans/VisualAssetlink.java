package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.svg.SvgProvider;

@HippoEssentialsGenerated(internalName = "website:visualassetlink")
@Node(jcrType = "website:visualassetlink")
public class VisualAssetlink extends Assetlink {

    @HippoEssentialsGenerated(internalName = "website:shortsummary")
    public String getSummary() {
        return getSingleProperty("website:shortsummary");
    }

    @HippoEssentialsGenerated(internalName = "website:icon")
    public CorporateWebsiteImageset getIcon() {
        return getLinkedBean("website:icon", CorporateWebsiteImageset.class);
    }

    public String getSvgXmlFromRepository() {
        HippoBean imageBean = getIcon();
        return SvgProvider.getSvgXmlFromBean(imageBean);
    }

}
