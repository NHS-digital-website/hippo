package uk.nhs.digital.ps.migrator.task;

public interface Task {

    void execute();

    boolean isRequested();
}
