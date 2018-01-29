package uk.nhs.digital.ps.migrator.model.hippo;

public class Series extends HippoImportableItem {

    private final String title;
    private final String summary;

    public Series(final Folder parentFolder,
                  final String cmsNodeDisplayName,
                  final String title,
                  final String summary) {
        super(parentFolder, cmsNodeDisplayName);
        this.title = title;
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }
}
