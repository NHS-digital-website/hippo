package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Calendar;


@HippoEssentialsGenerated(internalName = "website:cyberacknowledgement")
@Node(jcrType = "website:cyberacknowledgement")
public class CyberAcknowledgement extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:linkaddress")
    public String getLinkAddress() {
        return getProperty("website:linkaddress");
    }

    @HippoEssentialsGenerated(internalName = "website:responsedatetime")
    public Calendar getResponseDatetime() {
        return getProperty("website:responsedatetime");
    }

}
