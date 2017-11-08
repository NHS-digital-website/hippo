package uk.nhs.digital.ps.test.acceptance.models;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static uk.nhs.digital.ps.test.acceptance.models.PublicationState.PUBLISHED;

public class TimeSeries {

    private String name;
    private String title;

    private List<Publication> publications = new ArrayList<>();

    TimeSeries(final TimeSeriesBuilder timeSeriesBuilder) {
        name = timeSeriesBuilder.getName();
        title = timeSeriesBuilder.getTitle();
        publications = timeSeriesBuilder.getPublications();
    }

    public String getTitle() {
        return title;
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
