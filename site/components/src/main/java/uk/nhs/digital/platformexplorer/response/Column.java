package uk.nhs.digital.platformexplorer.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder
    ({
        "ColumnInfos"
    })
public class Column {

    /**
     * (Required)
     */
    @JsonProperty("ColumnInfos")
    private List<ColumnInfo> columnInfos = null;

    /**
     * (Required)
     */
    @JsonProperty("ColumnInfos")
    public List<ColumnInfo> getColumnInfos() {
        return columnInfos;
    }

    /**
     * (Required)
     */
    @JsonProperty("ColumnInfos")
    public void setColumnInfos(List<ColumnInfo> columnInfos) {
        this.columnInfos = columnInfos;
    }

}
