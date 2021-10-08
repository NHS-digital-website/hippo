package uk.nhs.digital.intranet.enums;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import uk.nhs.digital.intranet.beans.NewsInternal;
import uk.nhs.digital.intranet.beans.Task;
import uk.nhs.digital.website.beans.Blog;
import uk.nhs.digital.website.beans.Team;

import java.util.*;

public enum SearchArea {

    PEOPLE("People"),
    NEWS("News"),
    TASKS("Tasks"),
    TEAMS("Teams");

    public static final List<SearchArea> INDIVIDUAL_DOCUMENT_SEARCH_AREAS = Arrays
        .asList(SearchArea.NEWS, SearchArea.TASKS, SearchArea.TEAMS, SearchArea.PEOPLE);

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

    public static List<SearchArea> getSearchAreasFromList(List<String> textList) {
        List<SearchArea> list = new ArrayList<>();

        for (String text : textList) {
            for (SearchArea searchArea : SearchArea.values()) {
                if (searchArea.name().equalsIgnoreCase(text)) {
                    list.add(searchArea);
                }
            }
        }

        return list;
    }

    public static SearchArea fromQueryString(String text) {
        for (SearchArea searchArea : SearchArea.values()) {
            if (searchArea.name().equalsIgnoreCase(text)) {
                return searchArea;
            }
        }
        return null;
    }

    public static SearchArea getSearchAreaFromDocType(HippoBean hippoBean) {
        switch (hippoBean.getContentType()) {
            case "intranet:newsinternal":
            case "website:blog":
                return SearchArea.NEWS;
            case "intranet:task":
                return SearchArea.TASKS;
            case "website:team":
                return SearchArea.TEAMS;
            default:
                return null;
        }
    }

    public static List<Class> getDocTypesFromSearchAreas(List<SearchArea> searchAreas) {
        ArrayList<Class> docTypes = new ArrayList<>();

        searchAreas.forEach(searchArea -> {
            if (searchArea == SearchArea.NEWS) {
                docTypes.add(NewsInternal.class);
                docTypes.add(Blog.class);
            } else if (searchArea == SearchArea.TASKS) {
                docTypes.add(Task.class);
            } else if (searchArea == SearchArea.TEAMS) {
                docTypes.add(Team.class);
            }
        });

        return docTypes;
    }
}
