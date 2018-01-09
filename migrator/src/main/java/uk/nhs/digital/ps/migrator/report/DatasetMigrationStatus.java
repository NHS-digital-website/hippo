package uk.nhs.digital.ps.migrator.report;

public enum DatasetMigrationStatus {

    FIELD_MIGRATED("Field migrated"),
    FIELD_NOT_MIGRATED("Field not migrated"),
    DATASET_NOT_MIGRATED("Dataset not migrated"),
    ONE_DUPLICATE_DATASET_MIGRATED("One instance migrated");

    private final String description;

    DatasetMigrationStatus(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
