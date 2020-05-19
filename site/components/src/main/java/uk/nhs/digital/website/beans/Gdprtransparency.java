package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.*;

@HippoEssentialsGenerated(internalName = "website:gdprtransparency")
@Node(jcrType = "website:gdprtransparency")
public class Gdprtransparency extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:datacontroller")
    public String getDatacontroller() {
        return getSingleProperty("website:datacontroller");
    }

    @HippoEssentialsGenerated(internalName = "website:assetrefnumber")
    public String getAssetrefnumber() {
        return getSingleProperty("website:assetrefnumber");
    }

    @HippoEssentialsGenerated(internalName = "website:howuseinformation")
    public String getHowuseinformation() {
        return getSingleProperty("website:howuseinformation");
    }

    @HippoEssentialsGenerated(internalName = "website:lawfulbasis")
    public String getLawfulbasis() {
        return getSingleProperty("website:lawfulbasis");
    }

    @HippoEssentialsGenerated(internalName = "website:sensitivity")
    public Boolean getSensitivity() {
        return getSingleProperty("website:sensitivity");
    }

    @HippoEssentialsGenerated(internalName = "website:outsideuk")
    public String getOutsideuk() {
        return getSingleProperty("website:outsideuk");
    }

    @HippoEssentialsGenerated(internalName = "website:timeretained")
    public String getTimeretained() {
        return getSingleProperty("website:timeretained");
    }

    @HippoEssentialsGenerated(internalName = "website:rights")
    public String[] getRights() {
        return getMultipleProperty("website:rights");
    }

    @HippoEssentialsGenerated(internalName = "website:datasource")
    public String getDatasource() {
        return getSingleProperty("website:datasource");
    }

    @HippoEssentialsGenerated(internalName = "website:computerdecision")
    public String getComputerdecision() {
        return getSingleProperty("website:computerdecision");
    }

    @HippoEssentialsGenerated(internalName = "website:whocanaccessinfo")
    public HippoHtml getWhocanaccessinfo() {
        return getHippoHtml("website:whocanaccessinfo");
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
