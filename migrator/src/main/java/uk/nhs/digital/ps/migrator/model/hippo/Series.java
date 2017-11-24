package uk.nhs.digital.ps.migrator.model.hippo;

public class Series extends HippoImportableItem {

    private final String title;

    public Series(final Folder parentFolder,
                  final String cmsNodeDisplayName,
                  final String title) {
        super(parentFolder, cmsNodeDisplayName);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
