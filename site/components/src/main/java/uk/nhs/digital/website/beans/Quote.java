package uk.nhs.digital.website.beans;


import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:quote")
@Node(jcrType = "website:quote")
public class Quote extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:quote")
    public HippoHtml getQuote() {
        return getHippoHtml("website:quote");
    }

    @HippoEssentialsGenerated(internalName = "website:person")
    public String getPerson() {
        return getSingleProperty("website:person");
    }

    @HippoEssentialsGenerated(internalName = "website:role")
    public String getRole() {
        return getSingleProperty("website:role");
    }

    @HippoEssentialsGenerated(internalName = "website:organisation")
    public String getOrganisation() {
        return getSingleProperty("website:organisation");
    }

    public String getSectionType() {
        return "quote";
    }
}
