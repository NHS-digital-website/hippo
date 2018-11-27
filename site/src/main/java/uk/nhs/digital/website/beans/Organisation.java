package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;


@HippoEssentialsGenerated(internalName = "website:organisation")
@Node(jcrType = "website:organisation")
public class Organisation extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:abbreviation")
    public String[] getAbbreviation() {
        return getProperty("website:abbreviation");
    }

    @HippoEssentialsGenerated(internalName = "website:synonyms")
    public String[] getSynonyms() {
        return getProperty("website:synonyms");
    }

    @HippoEssentialsGenerated(internalName = "website:organisationType")
    public String[] getOrganisationType() {
        return getProperty("website:organisationType");
    }

    @HippoEssentialsGenerated(internalName = "website:parentorganisation")
    public HippoBean getParentOrganisation() {
        return getLinkedBean("website:parentorganisation", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:url")
    public String[] getUrl() {
        return getProperty("website:url");
    }

    @HippoEssentialsGenerated(internalName = "website:address")
    public List<HippoBean> getAddress() {
        return getChildBeansByName("website:address", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:organisationcodes")
    public List<HippoBean> getOrganisationCodes() {
        return getChildBeansByName("website:organisationcodes", HippoBean.class);
    }

}
