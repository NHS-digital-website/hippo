package uk.nhs.digital.ps.migrator.model.hippo;

public class Publication extends HippoImportableItem {

    private final String title;
    private final String informationType;

    public Publication(final Folder parent,
                       final String name,
                       final String title,
                       final String informationType
    ) {
        super(parent, name);
        this.title = title;
        this.informationType = informationType;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return title + " Summary";
    }

    public String getInformationType() {
        return informationType;
    }
}
