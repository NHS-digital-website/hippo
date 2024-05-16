package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.svg.SvgProvider;

@HippoEssentialsGenerated(internalName = "website:bannercontrol")
@Node(jcrType = "website:bannercontrol")
public class BannerControl extends BaseCompound {

    @Override
    public String getTitle() {
        return null;
    }

    public String getSvgXmlFromRepository() {
        HippoBean imageBean = getIcon();
        return SvgProvider.getSvgXmlFromBean(imageBean);
    }
}
