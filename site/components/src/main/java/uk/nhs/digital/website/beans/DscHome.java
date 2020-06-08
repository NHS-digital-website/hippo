package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:dschomepage")
@Node(jcrType = "website:dschomepage")
public class DscHome extends CommonFieldsBean {

    private List<HippoBean> cyberAlerts;

    @HippoEssentialsGenerated(internalName = "website:bannerimagelink")
    public CorporateWebsiteImageset getTrustimage() {
        return getLinkedBean("website:bannerimagelink", CorporateWebsiteImageset.class);
    }

    public void setCyberAlerts(List<HippoBean> cyberAlerts) {
        this.cyberAlerts = cyberAlerts;
    }

    public List<HippoBean> getCyberAlerts() {
        return cyberAlerts;
    }

}
