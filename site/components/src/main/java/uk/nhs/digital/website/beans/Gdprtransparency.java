package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
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
    public HippoHtml getHowuseinformation() {
        return getHippoHtml("website:howuseinformation");
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

    public List<GdprRights> getGdrpRights() {
        List<GdprRights> lsRights = new ArrayList();
        lsRights.add(getRights1());
        lsRights.add(getRights2());
        lsRights.add(getRights3());
        lsRights.add(getRights4());
        lsRights.add(getRights5());
        lsRights.add(getRights6());
        lsRights.add(getRights7());
        lsRights.add(getRights8());
        return lsRights;
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

    @HippoEssentialsGenerated(internalName = "website:beinformed")
    public GdprRights getRights1() {
        return getBean("website:beinformed", GdprRights.class);
    }

    @HippoEssentialsGenerated(internalName = "website:getaccesstoit")
    public GdprRights getRights2() {
        return getBean("website:getaccesstoit", GdprRights.class);
    }

    @HippoEssentialsGenerated(internalName = "website:rectifyorchange")
    public GdprRights getRights3() {
        return getBean("website:rectifyorchange", GdprRights.class);
    }

    @HippoEssentialsGenerated(internalName = "website:eraseorremove")
    public GdprRights getRights4() {
        return getBean("website:eraseorremove", GdprRights.class);
    }

    @HippoEssentialsGenerated(internalName = "website:restrictstoppro")
    public GdprRights getRights5() {
        return getBean("website:restrictstoppro", GdprRights.class);
    }

    @HippoEssentialsGenerated(internalName = "website:movecopytransfer")
    public GdprRights getRights6() {
        return getBean("website:movecopytransfer", GdprRights.class);
    }

    @HippoEssentialsGenerated(internalName = "website:objecttoit")
    public GdprRights getRights7() {
        return getBean("website:objecttoit", GdprRights.class);
    }

    @HippoEssentialsGenerated(internalName = "website:computerdecisionorperson")
    public GdprRights getRights8() {
        return getBean("website:computerdecisionorperson", GdprRights.class);
    }

    @HippoEssentialsGenerated(internalName = "website:sections")
    public List<HippoBean> getSections() {
        return getChildBeansByName("website:sections");
    }

}
