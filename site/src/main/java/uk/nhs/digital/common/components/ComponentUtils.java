/*
 * ComponentUtils.java
 */

package uk.nhs.digital.common.components;

import org.hippoecm.hst.util.SearchInputParsingUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ComponentUtils {

    private static final String WILDCARD_IN_USE_CHAR = "*";
    private static final int WILDCARD_POSTFIX_MIN_LENGTH = 3;

    /**
     * Convert query to include wildcard (*) character after each search term.
     * If the term is a phrase, the search is for an exact match, so no wildcard is required,
     * but the escaped backslash must be removed for the results to be accurate.
     * If the term ends with fullstop or comma, do not apply wildcard
     * <p>
     * e.g. lorem ipsum                 =>      lorem* ipsum*
     * "dolor sit" lorem ipsum     =>      "dolor sit" lorem* ipsum*
     * lor ipsum                   =>      lor* ipsum*
     */
    public static String parseAndApplyWildcards(String query) {

        ArrayList<String> terms = new ArrayList<>();

        // Split terms by space, treat phrases (wrapped in double quotes) as a single term
        Matcher matcher = Pattern.compile("(\"[^\"]*\")|([^\\s\"]+)").matcher(query);
        while (matcher.find()) {
            String term = null;
            if (matcher.group(1) != null) {
                // phrase that was quoted so add without wild card
                term = matcher.group(1);
            } else if (matcher.group(2) != null) {
                term = matcher.group(2);

                if (term.length() >= WILDCARD_POSTFIX_MIN_LENGTH
                    && !term.endsWith(".")
                    && !term.endsWith(",")) {
                    // single unquoted word so add wildcard
                    term = matcher.group(2) + WILDCARD_IN_USE_CHAR;
                }
            }

            terms.add(term);
        }

        query = String.join(" ", terms);

        // Escape any special chars but not the quotes, we have already dealt with those
        // Do this last to properly sanitize any input
        return SearchInputParsingUtils.parse(query, true, new char[]{'"'}, false);
    }
}


