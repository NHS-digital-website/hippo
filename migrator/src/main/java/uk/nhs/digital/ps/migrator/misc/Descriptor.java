package uk.nhs.digital.ps.migrator.misc;

public class Descriptor {

    private String term;
    private String description;

    private Descriptor(final String term, final String description) {
        this.term = term;
        this.description = description;
    }

    public static Descriptor describe(final String term, final Object description) {
        return new Descriptor(term, String.valueOf(description));
    }

    public String getTerm() {
        return term;
    }

    public String getDescription() {
        return description;
    }
}
