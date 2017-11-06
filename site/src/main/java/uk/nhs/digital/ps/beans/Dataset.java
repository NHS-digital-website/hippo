package uk.nhs.digital.ps.beans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import java.util.Calendar;
import java.util.List;
import org.hippoecm.hst.content.beans.standard.HippoResourceBean;

@HippoEssentialsGenerated(internalName = "publicationsystem:dataset")
@Node(jcrType = "publicationsystem:dataset")
public class Dataset extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "publicationsystem:Title")
    public String getTitle() {
        return getProperty("publicationsystem:Title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Summary")
    public String getSummary() {
        return getProperty("publicationsystem:Summary");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Purpose")
    public String getPurpose() {
        return getProperty("publicationsystem:Purpose");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:NominalDate")
    public Calendar getNominalDate() {
        return getProperty("publicationsystem:NominalDate");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:NextPublicationDate")
    public Calendar getNextPublicationDate() {
        return getProperty("publicationsystem:NextPublicationDate");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:GeographicCoverage")
    public String getGeographicCoverage() {
        return getProperty("publicationsystem:GeographicCoverage");
    }

    @HippoEssentialsGenerated(internalName = "hippotaxonomy:keys")
    public String[] getKeys() {
        return getProperty("hippotaxonomy:keys");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Files")
    public List<HippoResourceBean> getFiles() {
        return getChildBeansByName("publicationsystem:Files",
                HippoResourceBean.class);
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Granularity")
    public String[] getGranularity() {
        return getProperty("publicationsystem:Granularity");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:CoverageStart")
    public Calendar getCoverageStart() {
        return getProperty("publicationsystem:CoverageStart");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:CoverageEnd")
    public Calendar getCoverageEnd() {
        return getProperty("publicationsystem:CoverageEnd");
    }
}
