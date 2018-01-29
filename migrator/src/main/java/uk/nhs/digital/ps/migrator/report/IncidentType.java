package uk.nhs.digital.ps.migrator.report;

import static uk.nhs.digital.ps.migrator.report.DatasetMigrationImpact.*;

public enum IncidentType {

    BLANK_SUMMARY(
        FIELD_MIGRATED_AS_IS,
        "Dataset has blank summary"
    ),
    NO_DATASET_MAPPING(
        DATASET_NOT_MIGRATED,
        "No mapping found for Dataset",
        "",
        "Dataset was not included in any of the deliberately ignored sections"
            + "\n(Archive, etc.), neither was its P-code covered by any mapping."
            + "\n"
            + "\nDataset will not be imported, as it's unclear which publication"
            + "\nIt should belong to in the target system."
            + "\n"
            + "\nAn investigation and a decision on what to do with this dataset"
            + "\nare needed."
    ),
    DATE_WITH_EXTRA_TEXT(
        FIELD_MIGRATED_MODIFIED,
        "Date found with additional text",
        "Date field | Value found | Interpreted as",
        "Date in the exported data contained some extra text. The imported Dataset"
            + "\nwill have this text stripped out but will have the date value"
            + "\nitself."
            + "\n"
            + "\nA decision may be needed on whether to reflect the stripped"
            + "\ntext in the target system, somehow."
    ),
    NO_DATE_MAPPING(
        FIELD_NOT_MIGRATED,
        "No mapping found for date",
        "Date field | Value found",
        "Provided mapping for date values did not contain any entry for this date."
            + "\nThe Dataset will be migrated with corresponding date field not populated."
            + "\n"
            + "\nThe missing mapping needs to be provided or the date populated"
            + "\nmanually in the target system."

    ),
    ATTACHMENT_NOT_AVAILABLE(
        FIELD_NOT_MIGRATED,
        "Attachment not available",
        "Broken attachement download URL",
        "Hyperlink pointing to an attachement associated with given Dataset"
            + "\nwas not valid and the attachement could not be downloaded."
            + "\n"
            + "\nThe migrated Dataset will be missing this attachement."
            + "\n"
            + "\nA decision is needed on what to do with this attachemnt."
    ),
    HYPERLINKS_IN_SUMMARY(
        FIELD_MIGRATED_AS_IS,
        "Dataset summary contains hyperlink(s)",
        "Hyperlink(s) found",
        "Hyperlinks have been found in Dataset summary."
            + "\n"
            + "\nThey need to be manually added to the Dataset in the target system."
    ),
    HTML_IN_SUMMARY(
        FIELD_MIGRATED_AS_IS,
        "HTML suspected in Dataset summary",
        "Raw summary content",
        "Dataset summary has been found to contain characters which may indicate"
            + "\npresence of HTML tags. Since interpreting and stripping such tags"
            + "\nautomatically is very difficult, the report simply flags corresponding"
            + "\nDatasets for operator's attention."
            + "\n"
            + "\nA decision is needed on whether the summary can be migrated as is"
            + "\nor does it require some sanitizing."
    ),
    DUPLICATE_PCODE_IMPORTED( // found when scanning list of all the JSON files prepared for import
        FIELD_MIGRATED_AS_IS,
        "Duplicate P-code imported twice"
    ),
    DUPLICATE_PCODE_WITHIN_PUB(
        ONE_DUPLICATE_DATASET_MIGRATED,
        "P-code mapped twice to the same publication",
        "Publication title",
        "P-code was specified more than once in the mapping, with both instances mapped"
            + "\nto the same publication. Only one instance of the corresponding"
            + "\nDataset will actually be imported to the target system."
            + "\n"
            + "\nThe mapping needs reviewing and the duplication addressed."
    ),
    DATASET_MISSING_IN_MAPPING(
        DATASET_NOT_MIGRATED,
        "P-code present in mapping but not found in Migration Package",
        "Publication title from mapping"
    ),
    PCODE_IMPORT_STATUS_MISSING(
        DATASET_NOT_MIGRATED,
        "'Migrate Y/N' value missing from mapping",
        "Actual value from mapping"
    ),
    PCODE_WITH_INVALID_PARENT(
        DATASET_NOT_MIGRATED,
        "Invalid or missing definition of parent publication/series in mapping",
        "Actual values for series:publication found in the mapping"
    ),
    DATASET_CONVERSION_ERROR(
        DATASET_NOT_MIGRATED,
        "Dataset could not be converted",
        "Underlying exception"
    ), DATASET_IMPORT_FILE_FAILURE(
        DATASET_NOT_MIGRATED,
        "Failed to generate import file"
    ),
    RESOURCE_LINK_FILTERED_OUT(
        FIELD_NOT_MIGRATED,
        "Resource link filtered out",
        "Resource link 'title | value'",
        "Links associated with Datasets, where link name contains 'contact us'"
        + "\nor 'earlier data may be available'."
        + "\nshould not be migrated for Compendium datasets."
        + "\n"
        + "\nThis link will not be migrated automatically, it's left to the operator."
        + "\nto decide whether to add it manually post-migration."
    ),
    TAXONOMY_MAPPING_MISSING(
        FIELD_NOT_MIGRATED,
        "Taxonomy mapping missing for Dataset",
        "",
        "Datasets get Taxonomy terms applied as defined in the mapping file"
        + "\nfor each Dataset, individually but the provided mapping file"
        + "\n was missing mapping for given Dataset."
        + "\n"
        + "\nUpdated mapping file should be provided, one that includes"
        + "\nthe offending Dataset."
    ),
    TAXONOMY_MAPPING_INVALID(
        FIELD_MIGRATED_PARTIALLY,
        "Invalid Taxonomy mapping",
        "Provided Taxonomy values",
        "Taxonomy mapping provided for the Dataset contained invalid values"
        + "\n(ones absent from the Taxonomy definition)."
        + "\nValid values were migrated, invalid ones ignored."
        + "\n"
        + "\nProvided Taxonomy Mapping file needs reviewing in context of"
        + "\nTaxonomy Definition file and updated."
    ),
    TAXONOMY_MAPPING_DUPLICATE(
        FIELD_MIGRATED_AS_IS,
        "Duplicate Taxonomy mapping",
        "Duplicate mapping",
        "Provided Taxonomy mapping contains more than one mapping for the"
            + "\nsame dataset. The last mapping found will be used."
            + "\n"
            + "\nMapping file needs reviewing and updating."
    ),
    FIELD_MAPPING_DUPLICATE(
        FIELD_MIGRATED_AS_IS,
        "Duplicate Field mapping",
        "Duplicate mapping",
        "Provided Field mapping contains more than one mapping for the"
            + "\nsame dataset. The last mapping found will be used."
            + "\n"
            + "\nMapping file needs reviewing and updating."
    ),
    FIELD_MAPPING_MISSING(
        FIELD_NOT_MIGRATED,
        "Field mapping missing for Dataset",
            "",
            "Datasets get certain fields applied as defined in the mapping file"
            + "\nfor each Dataset but the provided mapping file"
            + "\n was missing mapping for given Dataset."
            + "\n"
            + "\nUpdated mapping file should be provided, one that includes"
            + "\nthe offending Dataset."
    ),
    FIELD_MAPPING_MISSING_FIELD(
        FIELD_NOT_MIGRATED,
        "Field mapping missing field for Dataset",
            "Missing Field",
            "Datasets get certain fields applied as defined in the mapping file"
            + "\nfor each Dataset but the provided mapping file"
            + "\n was missing mapping for a certian field for a given dataset."
            + "\n"
            + "\nUpdated mapping file should be provided, one that includes"
            + "\nthe missing field."
    ),
    FIELD_MAPPING_INVALID(
        FIELD_NOT_MIGRATED,
        "Field mapping is invalid for Dataset",
            "Field | Invalid data",
            "Datasets get certain fields applied as defined in the mapping file"
            + "\nfor each Dataset but the provided mapping file"
            + "\n contained an invalid mapping for a certian field for a given dataset."
            + "\n"
            + "\nUpdated mapping file should be provided."
    );

    private final DatasetMigrationImpact datasetMigrationImpact;
    private final String description;
    private final String supportingDataDescription;
    private final String extraDescription;

    IncidentType(final DatasetMigrationImpact datasetMigrationImpact,
                 final String description,
                 final String incidentDataDescription
    ) {
        this(datasetMigrationImpact, description, incidentDataDescription, "");
    }



    IncidentType(final DatasetMigrationImpact datasetMigrationImpact,
                 final String description) {
        this(datasetMigrationImpact, description, "", "");
    }

    IncidentType(final DatasetMigrationImpact datasetMigrationImpact,
                 final String description,
                 final String incidentDataDescription,
                 final String extraDescription) {
        this.datasetMigrationImpact = datasetMigrationImpact;
        this.description = description;
        this.supportingDataDescription = incidentDataDescription;
        this.extraDescription = extraDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getSupportingDataDescription() {
        return supportingDataDescription;
    }

    public DatasetMigrationImpact getDatasetMigrationImpact() {
        return datasetMigrationImpact;
    }

    public String getExtraDescription() {
        return extraDescription;
    }
}
