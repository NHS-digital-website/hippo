package uk.nhs.digital.ps.components;

import static java.lang.Character.isDigit;
import static java.lang.Integer.parseInt;

import uk.nhs.digital.ps.beans.BaseDocument;

import java.util.Comparator;

/**
 * Imported datasets are prefixed with numbers, we want to show them in this order (not strictly alphabetical or numberical):
 * 1.9 -> 1.10 -> 1.11 -> 2 -> bar -> foo
 */
public class DocumentTitleComparator implements Comparator<BaseDocument> {

    public static final DocumentTitleComparator COMPARATOR = new DocumentTitleComparator();

    @Override
    public int compare(BaseDocument d1, BaseDocument d2) {

        String s1 = d1.getTitle();
        String s2 = d2.getTitle();

        StringBuilder s1Part = new StringBuilder();
        StringBuilder s2Part = new StringBuilder();

        // + 1 so we compare the numbers at the very end of the string
        int max = Math.min(s1.length(), s2.length()) + 1;

        for (int i = 0; i < max; i++) {
            char c1 = getChar(s1, i);
            char c2 = getChar(s2, i);

            boolean c1Digit = isDigit(c1);
            boolean c2Digit = isDigit(c2);

            if (c1Digit && c2Digit) {
                s1Part.append(c1);
                s2Part.append(c2);
                continue;
            } else if (s1Part.length() > 0) {
                if (c1Digit) {
                    // c1 is a number and c2 isn't
                    return 1;
                } else if (c2Digit) {
                    // c2 is a number and c1 isn't
                    return -1;
                } else {
                    int diff = parseInt(s1Part.toString()) - parseInt(s2Part.toString());
                    if (diff != 0) {
                        return diff;
                    }
                    s1Part.setLength(0);
                    s2Part.setLength(0);
                }
            }

            if (c2 != c1) {
                return c1 - c2;
            }
        }

        // strings are the same up to the length of the shorter string
        return s2.length() - s1.length();
    }

    private char getChar(String string, int index) {
        return index < string.length() ? string.charAt(index) : 0;
    }
}
