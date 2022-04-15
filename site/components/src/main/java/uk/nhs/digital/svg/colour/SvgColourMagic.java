package uk.nhs.digital.svg.colour;

import uk.nhs.digital.svg.SvgProvider;

import static java.text.MessageFormat.format;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @deprecated
 * For future SVG usage, this class should not be used. Please upload svg images on the repository.
 * <p> Use {@link SvgProvider#getSvgXmlFromBean(org.hippoecm.hst.content.beans.standard.HippoBean)} instead.
 */
@Deprecated
public class SvgColourMagic {

    static final Pattern regex = Pattern.compile("#((?:[0-9a-fA-F]{3}){1,2})", Pattern.UNICODE_CASE);

    static String replaceColour(final String input, final String colour) {
        Matcher regexMatcher = regex.matcher(input);
        return regexMatcher.replaceAll(colour);
    }

    public static boolean isColour(String colour) {
        return format("#{0}", colour).matches(regex.toString());
    }

}
