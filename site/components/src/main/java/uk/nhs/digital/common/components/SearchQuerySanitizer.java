package uk.nhs.digital.common.components;

import com.google.common.base.Strings;
import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.util.SearchInputParsingUtils;
import org.onehippo.cms7.essentials.components.utils.SiteUtils;

import java.util.UUID;

public final class SearchQuerySanitizer {

    public static final String QUERY_PARAM = "query";
    public static final String SANITIZED_QUERY_ATTR = "sanitizedSearchQuery";
    public static final String SANITIZED_QUERY_DONE_ATTR = "sanitizedSearchQueryDone";
    public static final String UNSUPPORTED_QUERY_ATTR = "unsupportedSearchQuery";

    private SearchQuerySanitizer() {
    }

    public static String getSanitizedSearchQuery(HstRequest request, Object component) {

        if (Boolean.TRUE.equals(request.getAttribute(SANITIZED_QUERY_DONE_ATTR))) {
            return (String) request.getAttribute(SANITIZED_QUERY_ATTR);
        }

        String rawQuery = SiteUtils.getAnyParameter(QUERY_PARAM, request, (BaseHstComponent) component);

        request.removeAttribute(UNSUPPORTED_QUERY_ATTR);

        String sanitizedQuery = null;

        if (!Strings.isNullOrEmpty(rawQuery)) {
            String trimmedQuery = rawQuery.trim();

            if (isValidUuid(trimmedQuery)) {
                request.setAttribute(UNSUPPORTED_QUERY_ATTR, Boolean.TRUE);
                sanitizedQuery = trimmedQuery;

            } else if (isQuotedPhraseSearch(trimmedQuery)) {
                sanitizedQuery = trimmedQuery;

            } else {
                String parsed = SearchInputParsingUtils.parse(trimmedQuery, false);
                sanitizedQuery = Strings.emptyToNull(parsed);
            }
        }

        request.setAttribute(SANITIZED_QUERY_ATTR, sanitizedQuery);
        request.setAttribute(SANITIZED_QUERY_DONE_ATTR, Boolean.TRUE);

        return sanitizedQuery;
    }

    public static boolean isValidUuid(String value) {
        if (value == null) {
            return false;
        }

        try {
            UUID.fromString(value.trim());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isQuotedPhraseSearch(String query) {
        return query != null
            && query.length() >= 2
            && query.startsWith("\"")
            && query.endsWith("\"");
    }
}