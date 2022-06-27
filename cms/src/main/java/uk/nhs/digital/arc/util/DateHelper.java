package uk.nhs.digital.arc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Methods that can be used to manage dates for content items.
 */
public class DateHelper {
    private static final String TZ_SUFFIX = "T00:00:00.000Z";

    /**
     * Take a date of upwards of ten characters amd return it, if suitable, in a Timezone aware format
     * @param date is the date that is passed in. We expect at least YYYY-MM-DD format but can deal with
     *             slashes as well as extra characters
     * @return is eth date, set to midnight, that we determined from teh first ten characters supplied to us
     */
    public static String massageDate(String date) {

        if (date != null && date.length() >= 10) {
            String shortDate = date.substring(0, 10).replace("/","-");
            String regex = "(19|20)\\d\\d(-)(0[1-9]|1[012])\\2(0[1-9]|[12][0-9]|3[01])";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(shortDate);

            if (matcher.matches()) {
                return shortDate + TZ_SUFFIX;
            }
        }

        return "";
    }
}
