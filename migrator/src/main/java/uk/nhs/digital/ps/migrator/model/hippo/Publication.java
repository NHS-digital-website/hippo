package uk.nhs.digital.ps.migrator.model.hippo;

public class Publication extends HippoImportableItem {

    private final String title;

    public Publication(final Folder parent,
                       final String name,
                       final String title
    ) {
        super(parent, name);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
