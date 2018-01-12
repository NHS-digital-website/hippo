package uk.nhs.digital.ps.migrator.model.hippo;

import java.util.*;

public class DataSet extends HippoImportableItem {

    private final String pCode;
    private final String title;
    private final String summary;
    private final String nominalDate;
    private final String nextPublicationDate;
    private final List<Attachment> attachments;
    private final List<ResourceLink> resourceLinks;
    private final String taxonomyKeys;

    public DataSet(final Folder parentFolder,
                   final String pCode,
                   final String name,
                   final String title,
                   final String summary,
                   final String nominalDate,
                   final String nextPublicationDate,
                   final List<Attachment> attachments,
                   final List<ResourceLink> resourceLinks,
                   final String taxonomyKeys) {
        super(parentFolder, name);

        this.pCode = pCode;
        this.title = title;
        this.summary = summary;
        this.nominalDate = nominalDate;
        this.nextPublicationDate = nextPublicationDate;
        this.attachments = attachments;
        this.resourceLinks = resourceLinks;
        this.taxonomyKeys = taxonomyKeys;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public List<ResourceLink> getResourceLinks() {
        return resourceLinks;
    }

    public String getNominalDate() {
        return nominalDate;
    }

    public String getNextPublicationDate() {
        return nextPublicationDate;
    }

    public String getTaxonomyKeys() {
        return taxonomyKeys;
    }

    public String getPCode() {
        return pCode;
    }
}
