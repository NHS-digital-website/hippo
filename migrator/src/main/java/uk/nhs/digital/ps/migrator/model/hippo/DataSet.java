package uk.nhs.digital.ps.migrator.model.hippo;

public class DataSet extends HippoImportableItem {

    private final String title;

    public DataSet(final Folder parentFolder, final String name, final String title) {
        super(parentFolder, name);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
