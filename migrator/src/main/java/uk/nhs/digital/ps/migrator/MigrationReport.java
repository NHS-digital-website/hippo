package uk.nhs.digital.ps.migrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.migrator.config.ExecutionParameters;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;

import static java.nio.file.StandardOpenOption.*;

public final class MigrationReport {
    private static final Logger log = LoggerFactory.getLogger(MigrationReport.class);

    private final ExecutionParameters executionParameters;
    private final ArrayList<String> errors = new ArrayList<>();

    public MigrationReport(final ExecutionParameters executionParameters) {
        this.executionParameters = executionParameters;
    }

    public void add(String... output) {
        addToLog(null, output);
    }

    public void add(Exception e, String... output) {
        addToLog(e, output);
    }

    private void addToLog(Exception e, String... output) {
        String error = String.join("\n", output);
        if (e != null) {
            error += "\nException: " + e;
            Throwable cause = e.getCause();
            if (cause != null) {
                error += "\nCause: " + cause;
            }
        }

        log.error(error);
        errors.add(error);
    }

    public void writeToFile() {
        Collections.sort(errors);
        String toWrite = String.join("\n\n", errors);
        log.error("Migration Report Errors:\n" + toWrite);
        try {
            Files.write(executionParameters.getMigrationReportOutputPath(),
                toWrite.getBytes("UTF-8"),
                CREATE, TRUNCATE_EXISTING, WRITE);
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }
}
