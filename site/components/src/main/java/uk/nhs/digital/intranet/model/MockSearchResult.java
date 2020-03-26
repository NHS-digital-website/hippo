package uk.nhs.digital.intranet.model;

import uk.nhs.digital.intranet.enums.SearchResultType;

public class MockSearchResult implements SearchResult {

    private final String title;

    public MockSearchResult(final String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getType() {
        return SearchResultType.TEAM.getValue();
    }
}
