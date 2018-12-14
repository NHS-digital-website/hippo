package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:address")
@Node(jcrType = "website:address")
public class Address extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:address1")
    public String getAddress1() {
        return getProperty("website:address1");
    }

    @HippoEssentialsGenerated(internalName = "website:postcode")
    public String getPostcode() {
        return getProperty("website:postcode");
    }

    @HippoEssentialsGenerated(internalName = "website:extratext")
    public HippoHtml getExtratext() {
        return getHippoHtml("website:extratext");
    }
}
