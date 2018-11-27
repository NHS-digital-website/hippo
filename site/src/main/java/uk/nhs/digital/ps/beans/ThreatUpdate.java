package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Calendar;


@HippoEssentialsGenerated(internalName = "website:threatupdate")
@Node(jcrType = "website:threatupdate")
public class ThreatUpdate extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:content")
    public HippoHtml getContent() {
        return getHippoHtml("website:content");
    }

    @HippoEssentialsGenerated(internalName = "website:date")
    public Calendar getDate() {
        return getProperty("website:date");
    }

}
