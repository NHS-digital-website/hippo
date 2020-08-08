package uk.nhs.digital.platformexplorer.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder
    ({
        "Name",
        "SortOrder",
        "SortAscending"
    })
public class ColumnInfo {

    /**
     * (Required)
     */
    @JsonProperty("Name")
    private String name;
    /**
     * (Required)
     */
    @JsonProperty("SortOrder")
    private Object sortOrder;
    /**
     * (Required)
     */
    @JsonProperty("SortAscending")
    private Object sortAscending;

    /**
     * (Required)
     */
    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    /**
     * (Required)
     */
    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * (Required)
     */
    @JsonProperty("SortOrder")
    public Object getSortOrder() {
        return sortOrder;
    }

    /**
     * (Required)
     */
    @JsonProperty("SortOrder")
    public void setSortOrder(Object sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * (Required)
     */
    @JsonProperty("SortAscending")
    public Object getSortAscending() {
        return sortAscending;
    }

    /**
     * (Required)
     */
    @JsonProperty("SortAscending")
    public void setSortAscending(Object sortAscending) {
        this.sortAscending = sortAscending;
    }

}
