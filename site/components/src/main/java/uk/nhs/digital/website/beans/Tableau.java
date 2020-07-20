package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.net.MalformedURLException;
import java.net.URL;

@Node(jcrType = "website:tableau")
public class Tableau extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:device")
    public String getDevice() {
        return getSingleProperty("website:device");
    }

    @HippoEssentialsGenerated(internalName = "website:hidetabs")
    public Boolean getHidetabs() {
        return getSingleProperty("website:hidetabs");
    }

    @HippoEssentialsGenerated(internalName = "website:retry")
    public Long getRetry() {
        return getSingleProperty("website:retry");
    }

    @HippoEssentialsGenerated(internalName = "website:throttling")
    public Boolean getThrottling() {
        return getSingleProperty("website:throttling");
    }

    @HippoEssentialsGenerated(internalName = "website:throttlingLocation")
    public String getThrottlingLocation() {
        return getSingleProperty("website:throttlingLocation");
    }

    @HippoEssentialsGenerated(internalName = "website:url")
    public String getUrl() {
        return getSingleProperty("website:url");
    }

    public String getTableauBase() throws MalformedURLException {
        URL url = new URL(getUrl());
        return String.format("%s://%s/", url.getProtocol(), url.getAuthority());
    }

    public String getSectionType() {
        return "tableau";
    }
}
