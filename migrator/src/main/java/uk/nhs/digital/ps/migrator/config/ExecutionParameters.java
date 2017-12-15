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
    private Path nesstarAttachmentDownloadDir;
    private Path nesstarCompendiumMappingFile;

    private Path hippoImportDir;
    private Path taxonomySpreadsheetPath;
    private Path taxonomyOutputPath;
    private Path taxonomyMappingImportPath;

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

    public void setNesstarUnzipForce(final boolean isNesstarUnzipForce) {
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

    public Path getHippoImportDir() {
        return hippoImportDir;
    }

    public void setHippoImportDir(final Path hippoImportDir) {
        this.hippoImportDir = hippoImportDir;
    }

    public Path getNesstarAttachmentDownloadDir() {
        return nesstarAttachmentDownloadDir;
    }

    public void setNesstarAttachmentDownloadDir(Path nesstarAttachmentDownloadDir) {
        this.nesstarAttachmentDownloadDir = nesstarAttachmentDownloadDir;
    }

    public Path getNesstarCompendiumMappingFile() {
        return nesstarCompendiumMappingFile;
    }

    public void setNesstarCompendiumMappingFile(final Path nesstarCompendiumMappingFile) {
        this.nesstarCompendiumMappingFile = nesstarCompendiumMappingFile;
    }

    public Path getTaxonomySpreadsheetPath() {
        return taxonomySpreadsheetPath;
    }

    public void setTaxonomySpreadsheetPath(Path taxonomySpreadsheetPath) {
        this.taxonomySpreadsheetPath = taxonomySpreadsheetPath;
    }

    public Path getTaxonomyOutputPath() {
        return taxonomyOutputPath;
    }

    public void setTaxonomyOutputPath(Path taxonomyOutputPath) {
        this.taxonomyOutputPath = taxonomyOutputPath;
    }

    public Path getTaxonomyMappingImportPath() {
        return taxonomyMappingImportPath;
    }

    public void setTaxonomyMappingImportPath(Path taxonomyMappingImportPath) {
        this.taxonomyMappingImportPath = taxonomyMappingImportPath;
    }

    public List<Descriptor> descriptions() {
        return asList(
            describe("nesstarUnzippedExportDir", nesstarUnzippedExportDir),
            describe("nesstarZippedExportFile", nesstarZippedExportFile),
            describe("isNesstarUnzipForce", isNesstarUnzipForce),
            describe("convertNesstar", convertNesstar),
            describe("hippoImportDir", hippoImportDir),
            describe("nesstarAttachmentDownloadDir", nesstarAttachmentDownloadDir),
            describe("nesstarCompendiumMappingFile", nesstarCompendiumMappingFile),
            describe("taxonomySpreadsheetPath", taxonomySpreadsheetPath),
            describe("taxonomyOutputPath", taxonomyOutputPath),
            describe("taxonomyMappingImportPath", taxonomyMappingImportPath)
        );
    }
}
