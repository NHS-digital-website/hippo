package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.standard.HippoResourceBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import java.util.Calendar;
import java.util.List;
import org.hippoecm.hst.content.beans.standard.HippoResourceBean;
import uk.nhs.digital.ps.beans.Relatedlink;

import java.util.List;

@HippoEssentialsGenerated(internalName = "publicationsystem:publication")
@Node(jcrType = "publicationsystem:publication")
public class Publication extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "hippotaxonomy:keys")
    public String[] getKeys() {
        return getProperty("hippotaxonomy:keys");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Summary")
    public String getSummary() {
        return getProperty("publicationsystem:Summary");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:KeyFacts")
    public String getKeyFacts() {
        return getProperty("publicationsystem:KeyFacts");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:InformationType")
    public String[] getInformationType() {
        return getProperty("publicationsystem:InformationType");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:NominalDate")
    public Calendar getNominalDate() {
        return getProperty("publicationsystem:NominalDate");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:CoverageStart")
    public Calendar getCoverageStart() {
        return getProperty("publicationsystem:CoverageStart");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:CoverageEnd")
    public Calendar getCoverageEnd() {
        return getProperty("publicationsystem:CoverageEnd");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:GeographicCoverage")
    public String getGeographicCoverage() {
        return getProperty("publicationsystem:GeographicCoverage");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Granularity")
    public String[] getGranularity() {
        return getProperty("publicationsystem:Granularity");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:AdministrativeSources")
    public String getAdministrativeSources() {
        return getProperty("publicationsystem:AdministrativeSources");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Title")
    public String getTitle() {
        return getProperty("publicationsystem:Title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:RelatedLinks")
    public List<Relatedlink> getRelatedLinks() {
        return getChildBeansByName("publicationsystem:RelatedLinks",
                Relatedlink.class);
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:attachments")
    public List<HippoResourceBean> getAttachments() {
        return getChildBeansByName("publicationsystem:attachments", HippoResourceBean.class);
    }
}
