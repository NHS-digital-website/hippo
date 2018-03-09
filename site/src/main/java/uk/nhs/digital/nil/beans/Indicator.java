package uk.nhs.digital.nil.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import uk.nhs.digital.ps.beans.Attachment;
import uk.nhs.digital.ps.beans.HippoBeanHelper;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:indicator")
@Node(jcrType = "nationalindicatorlibrary:indicator")
public class Indicator extends BaseDocument {
    @HippoEssentialsGenerated(internalName = PropertyKeys.TOPBAR)
    public Topbar getTopbar() {
        return getBean(PropertyKeys.TOPBAR, Topbar.class);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.DETAILS)
    public Details getDetails() {
        return getBean(PropertyKeys.DETAILS, Details.class);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.ATTACHMENTS)
    public List<Attachment> getAttachments() {
        return getChildBeansByName(PropertyKeys.ATTACHMENTS, Attachment.class);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.TITLE)
    public String getTitle() {
        return getProperty(PropertyKeys.TITLE);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.ASSUREDSTATUS)
    public Boolean getAssuredStatus() {
        return getProperty(PropertyKeys.ASSUREDSTATUS);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.PUBLISHEDBY)
    public String getPublishedBy() {
        return getProperty(PropertyKeys.PUBLISHEDBY);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.REPORTINGLEVEL)
    public String getReportingLevel() {
        return getProperty(PropertyKeys.REPORTINGLEVEL);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.GEOGRAPHIC_COVERAGE)
    public String getGeographicCoverage() {
        return getProperty(PropertyKeys.GEOGRAPHIC_COVERAGE);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.TAXONOMY)
    public String[] getKeys() {
        return getProperty(PropertyKeys.TAXONOMY);
    }

    public List<String> getTaxonomyList() {
        List<List<String>> allTaxonomies = HippoBeanHelper.getTaxonomyList(getKeys());
        return allTaxonomies.stream().flatMap(x -> x.stream()).distinct().collect(Collectors.toList());
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.ASSURANCE_DATE)
    public Calendar getAssuranceDate() {
        return getProperty(PropertyKeys.ASSURANCE_DATE);
    }

    interface PropertyKeys {
        String TAXONOMY = "hippotaxonomy:keys";
        String ATTACHMENTS = "nationalindicatorlibrary:attachments";
        String DETAILS = "nationalindicatorlibrary:details";
        String TOPBAR = "nationalindicatorlibrary:topbar";
        String TITLE = "nationalindicatorlibrary:title";
        String ASSUREDSTATUS = "nationalindicatorlibrary:assuredStatus";
        String PUBLISHEDBY = "nationalindicatorlibrary:publishedBy"; 
        String REPORTINGLEVEL = "nationalindicatorlibrary:reportingLevel";     
        String GEOGRAPHIC_COVERAGE = "publicationsystem:GeographicCoverage";   
        String ASSURANCE_DATE = "nationalindicatorlibrary:assuranceDate";                
    }
}
