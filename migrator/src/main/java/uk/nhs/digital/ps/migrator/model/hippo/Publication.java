package uk.nhs.digital.ps.migrator.model.hippo;

public class Publication extends HippoImportableItem {

    private final String title;
    private final String informationType;
    private final String nominalDate;

    public Publication(final Folder parent,
                       final String name,
                       final String title,
                       final String informationType,
                       final String nominalDate) {
        super(parent, name);
        this.title = title;
        this.informationType = informationType;
        this.nominalDate = nominalDate;
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

    public String getNominalDate() {
        return nominalDate;
    }
}
