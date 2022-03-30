package uk.nhs.digital.intranet.enums;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import uk.nhs.digital.intranet.beans.NewsInternal;
import uk.nhs.digital.intranet.beans.Task;
import uk.nhs.digital.website.beans.Blog;
import uk.nhs.digital.website.beans.Team;

import java.util.*;

public enum SearchTypes {

    NEWS("News"),
    TASKS("Tasks"),
    TEAMS("Teams");

    public static final List<SearchTypes> INDIVIDUAL_DOCUMENT_SEARCH_TYPES = Arrays
        .asList(SearchTypes.NEWS, SearchTypes.TASKS, SearchTypes.TEAMS);

    public String getDisplayName() {
        return displayName;
    }

    final String displayName;

    SearchTypes(String displayName) {
        this.displayName = displayName;
    }

    public static List<SearchTypes> getSearchTypesFromList(List<String> textList) {
        List<SearchTypes> list = new ArrayList<>();

        for (String text : textList) {
            for (SearchTypes searchType : SearchTypes.values()) {
                if (searchType.name().equalsIgnoreCase(text)) {
                    list.add(searchType);
                }
            }
        }

        return list;
    }

    public static SearchTypes fromQueryString(String text) {
        for (SearchTypes searchArea : SearchTypes.values()) {
            if (searchArea.name().equalsIgnoreCase(text)) {
                return searchArea;
            }
        }
        return null;
    }

    public static SearchTypes getSearchTypeFromDocType(HippoBean hippoBean) {
        switch (hippoBean.getContentType()) {
            case "intranet:newsinternal":
            case "website:blog":
                return SearchTypes.NEWS;
            case "intranet:task":
                return SearchTypes.TASKS;
            case "website:team":
                return SearchTypes.TEAMS;
            default:
                return null;
        }
    }

    public static List<Class> getDocTypesFromSearchTypes(List<SearchTypes> searchTypes) {
        ArrayList<Class> docTypes = new ArrayList<>();

        searchTypes.forEach(searchType -> {
            if (searchType == SearchTypes.NEWS) {
                docTypes.add(NewsInternal.class);
                docTypes.add(Blog.class);
            } else if (searchType == SearchTypes.TASKS) {
                docTypes.add(Task.class);
            } else if (searchType == SearchTypes.TEAMS) {
                docTypes.add(Team.class);
            }
        });

        return docTypes;
    }
}
