package uk.nhs.digital.ps.migrator.task;

public interface MigrationTask {

    void execute();

    boolean isRequested();
}
