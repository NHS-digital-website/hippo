package uk.nhs.digital.svg.colour;

import static java.text.MessageFormat.format;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SvgColourMagic {

    static final Pattern regex = Pattern.compile("#((?:[0-9a-fA-F]{3}){1,2})", Pattern.UNICODE_CASE);

    public static String replaceColour(final String input, final String colour) {
        Matcher regexMatcher = regex.matcher(input);
        return regexMatcher.replaceAll(colour);
    }

    public static boolean isColour(String colour) {
        return format("#{0}", colour).matches(regex.toString());
    }

}
