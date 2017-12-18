package uk.nhs.digital.ps.migrator.model;

public class Property {

    private final String name;
    private final boolean multiple;
    private final String[] values;

    private final String type = "STRING";

    public Property(String name, boolean multiple, String value) {
        this.name = name;
        this.multiple = multiple;
        this.values = new String[]{value};
    }
}
