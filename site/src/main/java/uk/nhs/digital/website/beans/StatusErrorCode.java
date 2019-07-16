package uk.nhs.digital.website.beans;


import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:statuserrorcode")
@Node(jcrType = "website:statuserrorcode")
public class StatusErrorCode extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:description")
    public HippoHtml getDescription() {
        return getHippoHtml("website:description");
    }

    @HippoEssentialsGenerated(internalName = "website:howtofixthis")
    public HippoHtml getHowtofixthis() {
        return getHippoHtml("website:howtofixthis");
    }

    @HippoEssentialsGenerated(internalName = "website:meaning")
    public String getMeaning() {
        return getProperty("website:meaning");
    }

    @HippoEssentialsGenerated(internalName = "website:httpcode")
    public String getHttpcode() {
        return getProperty("website:httpcode");
    }

    @HippoEssentialsGenerated(internalName = "website:typeoferror")
    public String getTypeoferror() {
        return getProperty("website:typeoferror");
    }

    public String getSectionType() {
        return "statuserrorcode";
    }
}
