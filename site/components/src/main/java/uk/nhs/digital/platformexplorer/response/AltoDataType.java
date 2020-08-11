package uk.nhs.digital.platformexplorer.response;

import java.util.EnumSet;
import java.util.StringJoiner;

public enum AltoDataType {

    CAPABILITY("Capability", "Business service"),
    BUSINESS_SERVICE("Business service", "Application service"),
    APPLICATION_SERVICE("Application service", "Application component"),
    APPLICATION_COMPONENT("Application component", "System software"),
    SYSTEM_SOFTWARE("System software", null);

    private final String itemType;
    private final String childItemType;

    AltoDataType(String itemType, String childItemType) {
        this.itemType = itemType;
        this.childItemType = childItemType;
    }

    public static AltoDataType fromString(String name) {
        return EnumSet.allOf(AltoDataType.class).stream().filter(altoDataType -> name.equals(altoDataType.getItemType())).findAny().get();

    }

    public String getItemType() {
        return itemType;
    }

    public String getChildItemType() {
        return childItemType;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AltoDataType.class.getSimpleName() + "[", "]")
            .add("itemType='" + itemType + "'")
            .add("childItemType='" + childItemType + "'")
            .toString();
    }
}
