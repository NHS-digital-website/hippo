package uk.nhs.digital.ps.migrator.report;

public enum DatasetMigrationImpact {

    FIELD_MIGRATED_AS_IS("None; field migrated as is"),
    FIELD_MIGRATED_MODIFIED("Modified value migrated"),
    FIELD_NOT_MIGRATED("Field not migrated"),
    DATASET_NOT_MIGRATED("Dataset not migrated"),
    ONE_DUPLICATE_DATASET_MIGRATED("Dataset migrated once");

    private final String description;

    DatasetMigrationImpact(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
