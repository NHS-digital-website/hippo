package uk.nhs.digital.website.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.ps.beans.HippoBeanHelper;


import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

@JsonIgnoreProperties({"hippoDocumentBean", "hippoFolderBean", "leaf"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@HippoEssentialsGenerated(internalName = "website:cyberalert")
@Node(jcrType = "website:cyberalert")
public class CyberAlert extends CommonFieldsBean {

    //override for customize REST API purposes
    @JsonProperty
    @Override
    public String getSeosummaryJson() {
        if (getPublicallyAccessible()) {
            return getHippoHtmlContent("website:seosummary");
        }
        return null;
    }

    @JsonProperty
    @Override
    @HippoEssentialsGenerated(internalName = "website:shortsummary", allowModifications = false)
    public String getShortsummary() {
        if (getPublicallyAccessible()) {
            return getProperty("website:shortsummary");
        }
        return null;
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:threatid", allowModifications = false)
    public String getThreatId() {
        return getProperty("website:threatid");
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "publicationsystem:NominalDate", allowModifications = false)
    public Calendar getPublishedDate() {
        return getProperty("publicationsystem:NominalDate");
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:sourceofthreatupdates", allowModifications = false)
    public String[] getSourceOfThreatUpdates() {
        if (getFullAccess()) {
            return getProperty("website:sourceofthreatupdates");
        }
        return null;
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:cyberacknowledgement", allowModifications = false)
    public List<HippoBean> getCyberAcknowledgements() {
        if (getFullAccess()) {
            return getChildBeansByName("website:cyberacknowledgement");
        }
        return null;
    }

    @JsonProperty
    public String getBasePath() {
        if (getPublicallyAccessible() || getLimitedAccess()) {
            final HstRequestContext context = RequestContextProvider.get();
            if (context != null) {
                //DW-1077 - compose this documetn base URL for REST API purposes
                HttpServletRequest req = context.getServletRequest();
                StringBuffer url = req.getRequestURL();
                String base = url.substring(0, url.length() - req.getRequestURI().length() + req.getContextPath().length());

                //remove basepath part ('content/documents/corporate-website')
                //and compose full path
                String pathSuffix = this.getPath().replace("/" + context.getSiteContentBasePath(), "");
                //remove '/site/' if exists
                return (base + pathSuffix).replace("/site/", "/");
            }
        }
        return null;
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:severity", allowModifications = false)
    public String getSeverity() {
        if (getPublicallyAccessible()) {
            return getProperty("website:severity");
        }
        return null;
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:category", allowModifications = false)
    public String[] getCategory() {
        if (getPublicallyAccessible()) {
            return getProperty("website:category");
        }
        return null;
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:threattype", allowModifications = false)
    public String getThreatType() {
        if (getPublicallyAccessible()) {
            return getProperty("website:threattype");
        }
        return null;
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:threatvector", allowModifications = false)
    public String[] getThreatvector() {
        if (getPublicallyAccessible()) {
            return getProperty("website:threatvector");
        }
        return null;
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:threataffects")
    public List<HippoBean> getThreatAffects() {
        if (getPublicallyAccessible()) {
            return getChildBeansByName("website:threataffects", HippoBean.class);
        }
        return null;
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:section", allowModifications = false)
    public List<HippoBean> getSections() {
        if (getPublicallyAccessible()) {
            return getChildBeansByName("website:section");
        }
        return null;
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:threatupdates", allowModifications = false)
    public List<HippoBean> getThreatUpdates() {
        if (getPublicallyAccessible()) {
            return getChildBeansByName("website:threatupdates");
        }
        return null;
    }

    @JsonProperty("remediationIntro")
    public String getRemediationIntroJson() {
        if (getPublicallyAccessible()) {
            return getHippoHtmlContent("website:remediationintro");
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

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:remediationsteps", allowModifications = false)
    public List<RemediationStep> getRemediationSteps() {
        if (getPublicallyAccessible()) {
            return getChildBeansByName("website:remediationsteps", RemediationStep.class);
        }
        return null;
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:indicatorscompromisenew", allowModifications = false)
    public List<HippoBean> getIndicatorsCompromise() {
        if (getPublicallyAccessible()) {
            return getChildBeansByName("website:indicatorscompromisenew", HippoBean.class);
        }
        return null;
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:ncsclink", allowModifications = false)
    public String getNcscLink() {
        if (getPublicallyAccessible()) {
            return getProperty("website:ncsclink");
        }
        return null;
    }

    @JsonIgnore
    @HippoEssentialsGenerated(internalName = "website:service", allowModifications = false)
    public List<HippoBean> getServices() {
        if (getPublicallyAccessible()) {
            return getLinkedBeans("website:service", HippoBean.class);
        }
        return null;
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "hippotaxonomy:keys", allowModifications = false)
    public String[] getKeys() {
        if (getPublicallyAccessible()) {
            return getProperty("hippotaxonomy:keys");
        }
        return null;
    }

    @JsonProperty
    public List<String> getFullTaxonomyList() {
        if (getPublicallyAccessible()) {
            return HippoBeanHelper.getFullTaxonomyList(this);
        }
        return null;
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:cybercveidentifiers", allowModifications = false)
    public List<HippoBean> getCveIdentifiers() {
        if (getPublicallyAccessible()) {
            return getChildBeansByName("website:cybercveidentifiers");
        }
        return null;
    }

    @JsonIgnore
    private Boolean getLimitedAccess() {
        return ! getFullAccess();
    }

    @JsonIgnore
    private Boolean getFullAccess() {

        Boolean fullAccess = true;
        final HstRequestContext context = RequestContextProvider.get();
        if (context != null) {
            HttpServletRequest request = context.getServletRequest();
            String limitedParam = request.getParameter("_limited");
            if (limitedParam != null && limitedParam.equals("true")) {
                fullAccess = false;
            }
        }
        return fullAccess;
    }

    @JsonIgnore
    @HippoEssentialsGenerated(internalName = "website:publicallyaccessible", allowModifications = false)
    public Boolean getPublicallyAccessible() {

        Boolean publicallyaccessible = (Boolean)getProperty("website:publicallyaccessible");

        return publicallyaccessible && this.getFullAccess();
    }

    @JsonIgnore
    @HippoEssentialsGenerated(internalName = "website:archivecontent", allowModifications = false)
    public Boolean getArchiveContent() {

        Boolean archiveContent = (Boolean)getProperty("website:archivecontent");

        return archiveContent && this.getFullAccess();
    }

}
