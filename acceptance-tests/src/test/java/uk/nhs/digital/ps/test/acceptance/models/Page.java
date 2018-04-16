package uk.nhs.digital.ps.test.acceptance.models;

import uk.nhs.digital.ps.test.acceptance.models.section.BodySection;

import java.util.List;

public class Page {

    private String name;
    private String title;
    private List<BodySection> bodySections;

    Page(final PublicationPageBuilder builder) {
        name = builder.getName();
        title = builder.getTitle();
        bodySections = builder.getBodySections();
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public List<BodySection> getBodySections() {
        return bodySections;
    }
}
