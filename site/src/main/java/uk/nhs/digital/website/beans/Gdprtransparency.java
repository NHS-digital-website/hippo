package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.*;

@HippoEssentialsGenerated(internalName = "website:gdprtransparency")
@Node(jcrType = "website:gdprtransparency")
public class Gdprtransparency extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "website:seosummary")
    public String getSeosummary() {
        return getProperty("website:seosummary");
    }

    @HippoEssentialsGenerated(internalName = "website:shortsummary")
    public String getShortsummary() {
        return getProperty("website:shortsummary");
    }

    @HippoEssentialsGenerated(internalName = "website:summary")
    public String getSummary() {
        return getProperty("website:summary");
    }

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:datacontroller")
    public String getDatacontroller() {
        return getProperty("website:datacontroller");
    }

    @HippoEssentialsGenerated(internalName = "website:assetrefnumber")
    public String getAssetrefnumber() {
        return getProperty("website:assetrefnumber");
    }

    @HippoEssentialsGenerated(internalName = "website:howuseinformation")
    public String getHowuseinformation() {
        return getProperty("website:howuseinformation");
    }

    @HippoEssentialsGenerated(internalName = "website:lawfulbasis")
    public String getLawfulbasis() {
        return getProperty("website:lawfulbasis");
    }

    @HippoEssentialsGenerated(internalName = "website:sensitivity")
    public Boolean getSensitivity() {
        return getProperty("website:sensitivity");
    }

    @HippoEssentialsGenerated(internalName = "website:outsideuk")
    public String getOutsideuk() {
        return getProperty("website:outsideuk");
    }

    @HippoEssentialsGenerated(internalName = "website:timeretained")
    public String getTimeretained() {
        return getProperty("website:timeretained");
    }

    @HippoEssentialsGenerated(internalName = "website:rights")
    public String[] getRights() {
        return getProperty("website:rights");
    }

    @HippoEssentialsGenerated(internalName = "website:datasource")
    public String getDatasource() {
        return getProperty("website:datasource");
    }

    @HippoEssentialsGenerated(internalName = "website:computerdecision")
    public String getComputerdecision() {
        return getProperty("website:computerdecision");
    }

    @HippoEssentialsGenerated(internalName = "website:whocanaccess")
    public String getWhocanaccess() {
        return getProperty("website:whocanaccess");
    }

    @HippoEssentialsGenerated(internalName = "website:withdrawconsent")
    public HippoHtml getWithdrawconsent() {
        return getHippoHtml("website:withdrawconsent");
    }

    @HippoEssentialsGenerated(internalName = "website:legallywhy")
    public HippoHtml getLegallywhy() {
        return getHippoHtml("website:legallywhy");
    }

    @HippoEssentialsGenerated(internalName = "website:items")
    public List<?> getBlocks() {
        return getChildBeansByName("website:items");
    }
}
