package uk.nhs.digital.ps.test.acceptance.models;

import static java.util.stream.Collectors.toList;
import static uk.nhs.digital.ps.test.acceptance.models.PublicationState.PUBLISHED;

import java.util.ArrayList;
import java.util.List;

public class PublicationSeries {

    private String name;
    private String title;
    private String summary;

    private List<Publication> publications = new ArrayList<>();

    PublicationSeries(final PublicationSeriesBuilder publicationSeriesBuilder) {
        name = publicationSeriesBuilder.getName();
        title = publicationSeriesBuilder.getTitle();
        summary = publicationSeriesBuilder.getSummary();
        publications = publicationSeriesBuilder.getPublications();
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

    public String getUrlName() {
        return name.toLowerCase().replace(' ', '-');
    }


    public List<Publication> getReleasedPublicationsLatestFirst() {
        final List<Publication> expectedPublications = getPublications().stream()
            .filter(publication -> PUBLISHED.equals(publication.getState()))
            .collect(toList());

        // Sort expected publications with latest nominal dates at the top
        expectedPublications.sort((left, right) -> -1 * left.getNominalPublicationDate()
            .compareTo(right.getNominalPublicationDate()));

        return expectedPublications;
    }
}
