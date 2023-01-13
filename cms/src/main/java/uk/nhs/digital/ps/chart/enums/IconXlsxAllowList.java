package uk.nhs.digital.ps.chart.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum IconXlsxAllowList {

    INTRO("introText"),
    DATE("date"),
    NUMERATOR("numerator"),
    DENOMINATOR("denominator"),
    SUBJECT("subject"),
    DESCRIPTION("description");

    private final String entryType;

    IconXlsxAllowList(String entryType) {
        this.entryType = entryType;
    }

    public static List<String> getAllowedValuesList()
    {
        return Arrays.stream(IconXlsxAllowList.values()).map(value -> value.entryType.toUpperCase()).collect(Collectors.toList());
    }

}
