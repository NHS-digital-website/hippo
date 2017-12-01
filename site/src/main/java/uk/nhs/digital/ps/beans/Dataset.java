package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.standard.HippoFolder;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import java.util.Calendar;
import java.util.List;
import org.hippoecm.hst.content.beans.standard.HippoResourceBean;
import uk.nhs.digital.ps.beans.structuredText.StructuredText;

@HippoEssentialsGenerated(internalName = "publicationsystem:dataset")
@Node(jcrType = "publicationsystem:dataset")
public class Dataset extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "publicationsystem:Title")
    public String getTitle() {
        return getProperty("publicationsystem:Title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Summary")
    public StructuredText getSummary() {
        return new StructuredText(getProperty("publicationsystem:Summary"));
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

    /*
     * Migration: 20171208
     * New field: publicationsystem:Files-v2
     * Old field: publicationsystem:Files
     */
    @HippoEssentialsGenerated(internalName = "publicationsystem:Files-v2")
    public List<HippoResourceBean> getFiles() {
        List<HippoResourceBean> attachments = getChildBeansByName("publicationsystem:Files-v2", HippoResourceBean.class);

        if (attachments.isEmpty()) {
            attachments = getChildBeansByName("publicationsystem:Files", HippoResourceBean.class);
        }

        return attachments;
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

    public Publication getParentPublication() {
        Publication publicationBean = null;

        HippoFolder folder = (HippoFolder) getParentBean();
        while (!HippoBeanHelper.isRootFolder(folder)) {
            publicationBean = Publication.getPublicationInFolder(folder);

            if (publicationBean != null) {
                break;
            } else {
                folder = (HippoFolder) folder.getParentBean();
            }
        }

        return publicationBean;
    }
}
