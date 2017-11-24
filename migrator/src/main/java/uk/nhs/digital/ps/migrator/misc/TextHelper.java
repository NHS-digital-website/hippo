package uk.nhs.digital.ps.migrator.misc;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TextHelper {

    private static final MessageDigest SHA1 = getShaOne();


    public static String sanitiseNesstarXmlText(final String xmlText) {

        final String prefix = "¶en¤";
        final String suffix = "¤";

        String sanitisedText = xmlText;

        sanitisedText = sanitisedText.startsWith(prefix)
            ? sanitisedText.substring(prefix.length())
            : sanitisedText;

        sanitisedText = sanitisedText.endsWith(suffix)
            ? sanitisedText.substring(0, sanitisedText.length() - suffix.length())
            : sanitisedText;

        return sanitisedText;
    }

    public static String toLowerCaseDashedValue(final String input) {
        return input.toLowerCase()
            .replaceAll("[^a-z0-9]", "-")    // replace anything that isn't an alphanumeric with dash
            .replaceAll("-+", "-")           // eliminate duplicate dashes
            .replaceAll("(^-|-$)", "")       // eliminate leading or trailing dashes
            ;
    }

    public static String toShaOne(final String input) {

        final byte[] digest = SHA1.digest(input.getBytes(StandardCharsets.UTF_8));
        final StringBuilder sb = new StringBuilder();

        for (final byte aDigest : digest) {
            sb.append(Integer.toHexString(0xFF & aDigest));
        }

        return sb.toString();
    }

    private static MessageDigest getShaOne() {
        try {
            return MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toLowerCaseDashedShortValue(final String input) {
        return Arrays.stream(toLowerCaseDashedValue(input).split("-"))
            .map(word -> StringUtils.truncate(word, 3))
            .collect(Collectors.joining("-"));
    }
}
