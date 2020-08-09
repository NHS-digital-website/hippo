package uk.nhs.digital.platformexplorer.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder
    ({
        "Name",
        "ViewId",
        "Columns",
        "Items"
    })
@JsonIgnoreProperties(ignoreUnknown = true)
public class AaltoView {

    @JsonProperty("Name")
    private String name;
    @JsonProperty("ViewId")
    private String viewId;
    @JsonProperty("Columns")
    private List<Column> columns = null;
    @JsonProperty("Items")
    private List<Item> items = null;

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("ViewId")
    public String getViewId() {
        return viewId;
    }

    @JsonProperty("ViewId")
    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    @JsonProperty("Columns")
    public List<Column> getColumns() {
        return columns;
    }

    @JsonProperty("Columns")
    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    @JsonProperty("Items")
    public List<Item> getItems() {
        return items;
    }

    @JsonProperty("Items")
    public void setItems(List<Item> items) {
        this.items = items;
    }

}
