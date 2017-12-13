package uk.nhs.digital.ps.migrator.config;

import uk.nhs.digital.ps.migrator.misc.Descriptor;

import java.nio.file.Path;
import java.util.List;

import static java.util.Arrays.asList;
import static uk.nhs.digital.ps.migrator.misc.Descriptor.describe;

public class ExecutionParameters {

    private Path nesstarUnzippedExportDir;
    private Path nesstarZippedExportFile;
    private boolean isNesstarUnzipForce;
    private boolean convertNesstar;

    private Path hippoImportDir;
    private Path attachmentDownloadDir;

    public Path getNesstarUnzippedExportDir() {
        return nesstarUnzippedExportDir;
    }

    public void setNesstarUnzippedExportDir(final Path nesstarUnzippedExportDir) {
        this.nesstarUnzippedExportDir = nesstarUnzippedExportDir;
    }

    public void setNesstarZippedExportFile(final Path nesstarZippedExportFile) {
        this.nesstarZippedExportFile = nesstarZippedExportFile;
    }

    public Path getNesstarZippedExportFile() {
        return nesstarZippedExportFile;
    }

    public void setIsNesstarUnzipForce(final boolean isNesstarUnzipForce) {
        this.isNesstarUnzipForce = isNesstarUnzipForce;
    }

    public boolean isNesstarUnzipForce() {
        return isNesstarUnzipForce;
    }

    public boolean isConvertNesstar() {
        return convertNesstar;
    }

    public void setConvertNesstar(final boolean convertNesstar) {
        this.convertNesstar = convertNesstar;
    }

    public List<Descriptor> descriptions() {
        return asList(
            describe("nesstarUnzippedExportDir", nesstarUnzippedExportDir),
            describe("nesstarZippedExportFile", nesstarZippedExportFile),
            describe("isNesstarUnzipForce", isNesstarUnzipForce),
            describe("convertNesstar", convertNesstar),
            describe("hippoImportDir", hippoImportDir),
            describe("attachmentDownloadFolder", attachmentDownloadDir)
        );
    }

    public Path getHippoImportDir() {
        return hippoImportDir;
    }

    public void setHippoImportDir(final Path hippoImportDir) {
        this.hippoImportDir = hippoImportDir;
    }

    public Path getAttachmentDownloadDir() {
        return attachmentDownloadDir;
    }

    public void setAttachmentDownloadDir(Path attachmentDownloadDir) {
        this.attachmentDownloadDir = attachmentDownloadDir;
    }
}
