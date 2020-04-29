package uk.nhs.digital.intranet.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum SearchArea {

    ALL("All"),
    PEOPLE("People"),
    NEWS("News"),
    TASKS("Tasks"),
    TEAMS("Teams");

    private static final List<SearchArea> INDIVIDUAL_DOCUMENT_SEARCH_AREAS = Arrays
        .asList(SearchArea.NEWS, SearchArea.TASKS, SearchArea.TEAMS);

    public String getDisplayName() {
        return displayName;
    }

    final String displayName;

    SearchArea(String displayName) {
        this.displayName = displayName;
    }

    public static List<SearchArea> getDocumentSearchAreasWithout(SearchArea without) {
        List<SearchArea> list = new ArrayList<>(INDIVIDUAL_DOCUMENT_SEARCH_AREAS);
        list.remove(without);
        return list;
    }

    public static SearchArea fromQueryString(String text) {
        for (SearchArea searchArea : SearchArea.values()) {
            if (searchArea.name().equalsIgnoreCase(text)) {
                return searchArea;
            }
        }
        return SearchArea.ALL;
    }
}
