package uk.nhs.digital.intranet.model;

import uk.nhs.digital.intranet.enums.SearchArea;

import java.util.Objects;

public class SearchResultTab {

    public SearchResultTab(SearchArea tabName, int numberOfResults) {
        this.tabName = tabName;
        this.numberOfResults = numberOfResults;
    }

    private SearchArea tabName;
    private int numberOfResults;

    public String getTabName() {
        return tabName.getDisplayName();
    }

    public int getNumberOfResults() {
        return numberOfResults;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SearchResultTab that = (SearchResultTab) o;
        return numberOfResults == that.numberOfResults && tabName == that.tabName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tabName, numberOfResults);
    }
}
