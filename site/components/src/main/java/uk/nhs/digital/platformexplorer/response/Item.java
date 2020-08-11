package uk.nhs.digital.platformexplorer.response;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.Optional;

import java.util.List;
import java.util.StringJoiner;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder
    ({
        "iServerId",
        "Name",
        "Type",
        "Library",
        "Description",
        "ApprovalStatus",
        "CheckedOutBy",
        "IsLocked",
        "VersionNumber",
        "CreatedBy",
        "DateCreated",
        "ModifiedBy",
        "DateModified",
        "Attributes",
        "RelatedItems"
    })
public class Item {

    /**
     * (Required)
     */
    @JsonProperty("iServerId")
    private String id;
    /**
     * (Required)
     */
    @JsonProperty("Name")
    private String name;
    /**
     * (Required)
     */
    @JsonProperty("Type")
    private String type;
    /**
     * (Required)
     */
    @JsonProperty("Library")
    private String library;
    /**
     * (Required)
     */
    @JsonProperty("Description")
    private String description;
    /**
     * (Required)
     */
    @JsonProperty("ApprovalStatus")
    private String approvalStatus;
    /**
     * (Required)
     */
    @JsonProperty("CheckedOutBy")
    private Object checkedOutBy;
    /**
     * (Required)
     */
    @JsonProperty("IsLocked")
    private Boolean isLocked;
    /**
     * (Required)
     */
    @JsonProperty("VersionNumber")
    private Integer versionNumber;
    /**
     * (Required)
     */
    @JsonProperty("CreatedBy")
    private String createdBy;
    /**
     * (Required)
     */
    @JsonProperty("DateCreated")
    private String dateCreated;
    /**
     * (Required)
     */
    @JsonProperty("ModifiedBy")
    private String modifiedBy;
    /**
     * (Required)
     */
    @JsonProperty("DateModified")
    private String dateModified;
    /**
     * (Required)
     */
    @JsonIgnore
    @JsonProperty("Attributes")
    private Attributes attributes;

    @JsonIgnore
    @JsonProperty("RelatedItems")
    private List<Item> relatedItems = null;

    /**
     * (Required)
     */
    @JsonProperty("iServerId")
    public String getId() {
        return id;
    }

    /**
     * (Required)
     */
    @JsonProperty("iServerId")
    public void setId(String id) {
        this.id = id;
    }

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
    @JsonProperty("Type")
    public String getType() {
        return type;
    }

    /**
     * (Required)
     */
    @JsonProperty("Type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     * (Required)
     */
    @JsonProperty("Library")
    public String getLibrary() {
        return library;
    }

    /**
     * (Required)
     */
    @JsonProperty("Library")
    public void setLibrary(String library) {
        this.library = library;
    }

    /**
     * (Required)
     */
    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    /**
     * (Required)
     */
    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * (Required)
     */
    @JsonProperty("ApprovalStatus")
    public String getApprovalStatus() {
        return approvalStatus;
    }

    /**
     * (Required)
     */
    @JsonProperty("ApprovalStatus")
    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    /**
     * (Required)
     */
    @JsonProperty("CheckedOutBy")
    public Object getCheckedOutBy() {
        return checkedOutBy;
    }

    /**
     * (Required)
     */
    @JsonProperty("CheckedOutBy")
    public void setCheckedOutBy(Object checkedOutBy) {
        this.checkedOutBy = checkedOutBy;
    }

    /**
     * (Required)
     */
    @JsonProperty("IsLocked")
    public Boolean getIsLocked() {
        return isLocked;
    }

    /**
     * (Required)
     */
    @JsonProperty("IsLocked")
    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    /**
     * (Required)
     */
    @JsonProperty("VersionNumber")
    public Integer getVersionNumber() {
        return versionNumber;
    }

    /**
     * (Required)
     */
    @JsonProperty("VersionNumber")
    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    /**
     * (Required)
     */
    @JsonProperty("CreatedBy")
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * (Required)
     */
    @JsonProperty("CreatedBy")
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * (Required)
     */
    @JsonProperty("DateCreated")
    public String getDateCreated() {
        return dateCreated;
    }

    /**
     * (Required)
     */
    @JsonProperty("DateCreated")
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * (Required)
     */
    @JsonProperty("ModifiedBy")
    public String getModifiedBy() {
        return modifiedBy;
    }

    /**
     * (Required)
     */
    @JsonProperty("ModifiedBy")
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * (Required)
     */
    @JsonProperty("DateModified")
    public String getDateModified() {
        return dateModified;
    }

    /**
     * (Required)
     */
    @JsonProperty("DateModified")
    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    /**
     * (Required)
     */
    @JsonIgnore
    @JsonProperty("Attributes")
    public Optional<Attributes> getAttributes() {
        return Optional.fromNullable(attributes);
    }

    /**
     * (Required)
     */
    @JsonProperty("Attributes")
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @JsonIgnore
    @JsonProperty("RelatedItems")
    public List<Item> getRelatedItems() {
        return relatedItems;
    }


    @JsonProperty("RelatedItems")
    public void setRelatedItems(List<Item> relatedItems) {
        this.relatedItems = relatedItems;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Item.class.getSimpleName() + "[", "]")
            .add("iServerId='" + id + "'")
            .add("name='" + name + "'")
            .add("type='" + type + "'")
            .add("library='" + library + "'")
            .add("description='" + description + "'")
            .add("approvalStatus='" + approvalStatus + "'")
            .add("checkedOutBy=" + checkedOutBy)
            .add("isLocked=" + isLocked)
            .add("versionNumber=" + versionNumber)
            .add("createdBy='" + createdBy + "'")
            .add("dateCreated='" + dateCreated + "'")
            .add("modifiedBy='" + modifiedBy + "'")
            .add("dateModified='" + dateModified + "'")
            .add("attributes=" + attributes)
            .toString();
    }
}
