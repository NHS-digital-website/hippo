package uk.nhs.digital.ps.migrator.model;

import java.util.ArrayList;
import uk.nhs.digital.ps.migrator.model.Property;

public class HippoNode {

    private final String name;
    private final String primaryType;
    private final Property[] properties;
    private final ArrayList<Object> nodes = new ArrayList<>();

    public HippoNode(String name, String primaryType, Property... properties) {
        this.name = name;
        this.primaryType = primaryType;
        this.properties = properties;
    }

    public void addNode(HippoNode node) {
        nodes.add(node);
    }

    public String getName() {
        return name;
    }
}
