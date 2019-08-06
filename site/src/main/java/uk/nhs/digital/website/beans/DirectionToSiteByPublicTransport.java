package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:directiontositebypublictransport")
@Node(jcrType = "website:directiontositebypublictransport")
public class DirectionToSiteByPublicTransport extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:publictransporttype")
    public String getPublictransportType() {
        return getProperty("website:publictransporttype");
    }

    @HippoEssentialsGenerated(internalName = "website:bypublictransporttext")
    public HippoHtml getBypublictransporttext() {
        return getHippoHtml("website:bypublictransporttext");
    }

    @HippoEssentialsGenerated(internalName = "website:publictransportstations")
    public List<HippoBean> getPublicTransportStations() {
        return getChildBeansByName("website:publictransportstations");
    }

    public String getSectionType() {
        return "directiontositebypublictransport";
    }

}
