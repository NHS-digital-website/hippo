package uk.nhs.digital.ps.chart.model;

import uk.nhs.digital.ps.chart.enums.IconType;

public class Icon {
    private final String type;

    public Icon(IconType type) {
        this.type = type.getIconType();
    }

    public String getType() {
        return type;
    }
}
