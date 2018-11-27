package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.ps.beans.HippoBeanHelper;

import java.util.Calendar;
import java.util.List;


@HippoEssentialsGenerated(internalName = "website:cyberalert")
@Node(jcrType = "website:cyberalert")
public class CyberAlert extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:threatid")
    public String getThreatId() {
        return getProperty("website:threatid");
    }

    @HippoEssentialsGenerated(internalName = "website:severity")
    public String getSeverity() {
        return getProperty("website:severity");
    }

    @HippoEssentialsGenerated(internalName = "website:category")
    public String[] getCategory() {
        return getProperty("website:category");
    }

    @HippoEssentialsGenerated(internalName = "website:threattype")
    public String getThreatType() {
        return getProperty("website:threattype");
    }

    @HippoEssentialsGenerated(internalName = "website:threatvector")
    public String[] getThreatvector() {
        return getProperty("website:threatvector");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:NominalDate")
    public Calendar getPublishedDate() {
        return getProperty("publicationsystem:NominalDate");
    }

    @HippoEssentialsGenerated(internalName = "website:threataffects")
    public List<HippoBean> getThreatAffects() {
        return getChildBeansByName("website:threataffects", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:section")
    public List<HippoBean> getSections() {
        return getChildBeansByName("website:section");
    }

    @HippoEssentialsGenerated(internalName = "website:threatupdates")
    public List<HippoBean> getThreatUpdates() {
        return getChildBeansByName("website:threatupdates");
    }

    @HippoEssentialsGenerated(internalName = "website:remediationintro")
    public HippoHtml getRemediationIntro() {
        return getHippoHtml("website:remediationintro");
    }

    @HippoEssentialsGenerated(internalName = "website:remediationsteps")
    public List<HippoBean> getRemediationSteps() {
        return getChildBeansByName("website:remediationsteps");
    }

    @HippoEssentialsGenerated(internalName = "website:indicatorscompromise")
    public HippoHtml getIndicatorsCompromise() {
        return getHippoHtml("website:indicatorscompromise");
    }

    @HippoEssentialsGenerated(internalName = "website:ncsclink")
    public String getNcscLink() {
        return getProperty("website:ncsclink");
    }

    @HippoEssentialsGenerated(internalName = "website:sourceofthreatupdates")
    public String[] getSourceOfThreatUpdates() {
        return getProperty("website:sourceofthreatupdates");
    }

    @HippoEssentialsGenerated(internalName = "website:service")
    public List<HippoBean> getServices() {
        return getLinkedBeans("website:service", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "hippotaxonomy:keys")
    public String[] getKeys() {
        return getProperty("hippotaxonomy:keys");
    }

    public List<String> getFullTaxonomyList() {
        return HippoBeanHelper.getFullTaxonomyList(this);
    }

    @HippoEssentialsGenerated(internalName = "website:cybercveidentifiers")
    public List<HippoBean> getCveIdentifiers() {
        return getChildBeansByName("website:cybercveidentifiers");
    }

    @HippoEssentialsGenerated(internalName = "website:cyberacknowledgement")
    public List<HippoBean> getCyberAcknowledgements() {
        return getChildBeansByName("website:cyberacknowledgement");
    }

    @HippoEssentialsGenerated(internalName = "website:publicallyaccessible")
    public Boolean getPublicallyAccessible() {
        return getProperty("website:publicallyaccessible");
    }


}
