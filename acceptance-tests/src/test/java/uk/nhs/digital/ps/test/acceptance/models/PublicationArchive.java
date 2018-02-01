package uk.nhs.digital.ps.test.acceptance.models;

import java.util.ArrayList;
import java.util.List;

public class PublicationArchive {

    private String name;
    private String title;
    private String summary;

    private List<Publication> publications = new ArrayList<>();

    PublicationArchive(final PublicationArchiveBuilder publicationArchiveBuilder) {
        name = publicationArchiveBuilder.getName();
        title = publicationArchiveBuilder.getTitle();
        summary = publicationArchiveBuilder.getSummary();
        publications = publicationArchiveBuilder.getPublications();
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public List<Publication> getPublications() {
        return new ArrayList<>(publications);
    }
}
