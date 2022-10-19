package uk.nhs.digital.arc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Methods that can be used to manage dates for content items.
 */
public class DateHelper {
    private static final String DEFAULT_HH_MM_SUFFIX = "T00:00";
    private static final String DEFAULT_SS_SUFFIX = ":00";
    private static final String DEFAULT_TZ_SUFFIX = ".000Z";

    private static String FULL_REGEX = "(19|20)\\d\\d(-)(0[1-9]|1[012])\\2(0[1-9]|[12][0-9]|3[01])(T[0-2][0-2]\\:[0-5][0-9](\\:[0-5][0-9])?)*";

    /**
     * Take a date-time of upwards of ten characters amd return it, if suitable, in a Timezone aware format
     * @param date is the date that is passed in. We expect at least YYYY-MM-DD format but can deal with
     *             slashes as well as time values
     * @return is the date in full TZ format
     */
    public static String massageDate(String date) {
        if (date != null && date.trim().length() >= 10) {
            String firstDate = date.trim().replace("/","-").replace(" ", "T");

            Pattern pattern = Pattern.compile(FULL_REGEX);
            Matcher matcher = pattern.matcher(firstDate);

            if (matcher.matches()) {
                return fullLengthDate(firstDate);
            }
        }

        return null;
    }

    /**
     * We get a wel formatted date but it may be opartial, so we'll pad it out with default values if required
     * @param theDate is the String version of the date. It may be partial
     * @return The formatted version of the date including hours, minutes and seconds as well as TZ info
     */
    private static String fullLengthDate(String theDate) {
        switch (theDate.length()) {
            // Full date but no time, so send back default time
            case 10: return theDate + DEFAULT_HH_MM_SUFFIX + DEFAULT_SS_SUFFIX + DEFAULT_TZ_SUFFIX;

            // Full date and hh:mm for time, so send back default seconds
            case 16: return theDate + DEFAULT_SS_SUFFIX + DEFAULT_TZ_SUFFIX;

            // Full date and hh:mm:ss, so just add TZ SUFFIZ
            case 19: return theDate + DEFAULT_TZ_SUFFIX;

            default: return null;
        }
    }
}
