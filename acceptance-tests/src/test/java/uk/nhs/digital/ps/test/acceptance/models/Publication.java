package uk.nhs.digital.ps.test.acceptance.models;

import java.util.UUID;

public class Publication {

    private String publicationName;
    private String publicationTitle;
    private String publicationSummary;

    public Publication() {
        publicationName = UUID.randomUUID().toString();
        publicationTitle = "Some title text";
        publicationSummary = "Some summary text";
    }

    public String getPublicationName() {
        return publicationName;
    }

    public String getPublicationTitle() {
        return publicationTitle;
    }

    public String getPublicationSummary() {
        return publicationSummary;
    }
}
