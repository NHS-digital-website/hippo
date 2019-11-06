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

    @HippoEssentialsGenerated(internalName = "website:threatid", allowModifications = false)
    public String getThreatId() {
        return getProperty("website:threatid");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:NominalDate", allowModifications = false)
    public Calendar getPublishedDate() {
        return getProperty("publicationsystem:NominalDate");
    }

    @HippoEssentialsGenerated(internalName = "website:sourceofthreatupdates", allowModifications = false)
    public String[] getSourceOfThreatUpdates() {
        return getProperty("website:sourceofthreatupdates");
    }

    @HippoEssentialsGenerated(internalName = "website:cyberacknowledgement", allowModifications = false)
    public List<HippoBean> getCyberAcknowledgements() {
        return getChildBeansByName("website:cyberacknowledgement");
    }

    @HippoEssentialsGenerated(internalName = "website:severity", allowModifications = false)
    public String getSeverity() {
        if (getPublicallyAccessible()) {
            return getProperty("website:severity");
        }
        return null;
    }

    @HippoEssentialsGenerated(internalName = "website:category", allowModifications = false)
    public String[] getCategory() {
        if (getPublicallyAccessible()) {
            return getProperty("website:category");
        }
        return null;
    }

    @HippoEssentialsGenerated(internalName = "website:threattype", allowModifications = false)
    public String getThreatType() {
        if (getPublicallyAccessible()) {
            return getProperty("website:threattype");
        }
        return null;
    }

    @HippoEssentialsGenerated(internalName = "website:threatvector", allowModifications = false)
    public String[] getThreatvector() {
        if (getPublicallyAccessible()) {
            return getProperty("website:threatvector");
        }
        return null;
    }

    @HippoEssentialsGenerated(internalName = "website:threataffects", allowModifications = false)
    public List<HippoBean> getThreatAffects() {
        if (getPublicallyAccessible()) {
            return getChildBeansByName("website:threataffects", HippoBean.class);
        }
        return null;
    }

    @HippoEssentialsGenerated(internalName = "website:section", allowModifications = false)
    public List<HippoBean> getSections() {
        if (getPublicallyAccessible()) {
            return getChildBeansByName("website:section");
        }
        return null;
    }

    @HippoEssentialsGenerated(internalName = "website:threatupdates", allowModifications = false)
    public List<HippoBean> getThreatUpdates() {
        if (getPublicallyAccessible()) {
            return getChildBeansByName("website:threatupdates");
        }
        return null;
    }

    @HippoEssentialsGenerated(internalName = "website:remediationintro", allowModifications = false)
    public HippoHtml getRemediationIntro() {
        if (getPublicallyAccessible()) {
            return getHippoHtml("website:remediationintro");
        }
        return null;
    }

    @HippoEssentialsGenerated(internalName = "website:remediationsteps", allowModifications = false)
    public List<HippoBean> getRemediationSteps() {
        if (getPublicallyAccessible()) {
            return getChildBeansByName("website:remediationsteps");
        }
        return null;
    }

    @HippoEssentialsGenerated(internalName = "website:indicatorscompromisenew", allowModifications = false)
    public List<HippoBean> getIndicatorsCompromise() {
        if (getPublicallyAccessible()) {
            return getChildBeansByName("website:indicatorscompromisenew");
        }
        return null;
    }


    @HippoEssentialsGenerated(internalName = "website:ncsclink", allowModifications = false)
    public String getNcscLink() {
        if (getPublicallyAccessible()) {
            return getProperty("website:ncsclink");
        }
        return null;
    }

    @HippoEssentialsGenerated(internalName = "website:service", allowModifications = false)
    public List<HippoBean> getServices() {
        if (getPublicallyAccessible()) {
            return getLinkedBeans("website:service", HippoBean.class);
        }
        return null;
    }

    @HippoEssentialsGenerated(internalName = "hippotaxonomy:keys", allowModifications = false)
    public String[] getKeys() {
        if (getPublicallyAccessible()) {
            return getProperty("hippotaxonomy:keys");
        }
        return null;
    }

    public List<String> getFullTaxonomyList() {
        if (getPublicallyAccessible()) {
            return HippoBeanHelper.getFullTaxonomyList(this);
        }
        return null;
    }

    @HippoEssentialsGenerated(internalName = "website:cybercveidentifiers", allowModifications = false)
    public List<HippoBean> getCveIdentifiers() {
        if (getPublicallyAccessible()) {
            return getChildBeansByName("website:cybercveidentifiers");
        }
        return null;
    }

    @HippoEssentialsGenerated(internalName = "website:publicallyaccessible", allowModifications = false)
    public Boolean getPublicallyAccessible() {
        return getProperty("website:publicallyaccessible");
    }

}
