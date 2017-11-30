package uk.nhs.digital.ps.beans.structuredText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Element {

    protected String type;

    public Element(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
