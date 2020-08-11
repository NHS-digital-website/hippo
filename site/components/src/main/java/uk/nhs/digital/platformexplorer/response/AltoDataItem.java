package uk.nhs.digital.platformexplorer.response;

public enum AltoDataItem {

    CAPABILITY("Capability") {
    },
    BUSINESS_SERVICE("Business Service") {
    },
    APPLICATION_SERVICE("Application Service") {
    },
    APPLICATION_COMPONENT("Application component") {
    },
    SYSTEM_SOFTWARE("System software") {
    };


    private final String itemType;

    AltoDataItem(String itemType) {
        this.itemType = itemType;
    }

    public static AltoDataItem fromString(String name) {
        return Enum.valueOf(AltoDataItem.class, name);
    }

    public String getItemType() {
        return itemType;
    }
}
