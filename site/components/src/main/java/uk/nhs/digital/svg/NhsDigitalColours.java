package uk.nhs.digital.svg;

public enum NhsDigitalColours {
    BLUE("Blue", "005EB8"),
    YELLOW("Yellow", "FFB81C"),
    WHITE("White", "FFFFFF"),
    BLACK("Black", "000000");

    private final String name;
    private final String hex;

    NhsDigitalColours(String name, String hex) {
        this.name = name;
        this.hex = hex;
    }

    public String formattedName() {
        return name;
    }

    public String getHex() {
        return hex;
    }

    public String getHashedHex() {
        return "#" + hex;
    }

    public static NhsDigitalColours fromString(String text) {
        for (NhsDigitalColours c : NhsDigitalColours.values()) {
            if (c.name.equalsIgnoreCase(text)) {
                return c;
            }
        }
        return null;
    }

}
