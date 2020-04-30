package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.ps.beans.HippoBeanHelper;

import java.util.List;


@HippoEssentialsGenerated(internalName = "website:platform")
@Node(jcrType = "website:platform")
public class Platform extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:abbreviation")
    public String[] getAbbreviation() {
        return getMultipleProperty("website:abbreviation");
    }

    @HippoEssentialsGenerated(internalName = "website:synonyms")
    public String[] getSynonyms() {
        return getMultipleProperty("website:synonyms");
    }

    @HippoEssentialsGenerated(internalName = "website:platformversions")
    public List<HippoBean> getPlatformVersions() {
        return getChildBeansByName("website:platformversions", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:url")
    public String getUrl() {
        return getSingleProperty("website:url");
    }

    @HippoEssentialsGenerated(internalName = "website:supplier")
    public HippoBean getSupplier() {
        return getLinkedBean("website:supplier", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:reseller")
    public List<HippoBean> getReseller() {
        return getChildBeansByName("website:reseller", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:parentplatforms")
    public List<HippoBean> getParentPlatforms() {
        return getChildBeansByName("website:parentplatforms", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "hippotaxonomy:keys")
    public String[] getKeys() {
        return getMultipleProperty("hippotaxonomy:keys");
    }

    public List<String> getFullTaxonomyList() {
        return HippoBeanHelper.getFullTaxonomyList(this);
    }

}
