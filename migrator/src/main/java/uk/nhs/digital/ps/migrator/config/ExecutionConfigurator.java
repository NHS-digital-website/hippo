package uk.nhs.digital.ps.migrator.config;

import org.springframework.boot.ApplicationArguments;
import uk.nhs.digital.ps.migrator.misc.Descriptor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static uk.nhs.digital.ps.migrator.misc.Descriptor.describe;

public class ExecutionConfigurator {

    public static final String HELP_FLAG = "help";

    private static final String NESSTAR_ZIP_FILE_PATH = "nesstarUnzipFrom";
    private static final String NESSTAR_FORCE_UNZIP_FLAG = "nesstarForceUnzip";
    private static final String NESSTAR_CONVERT_FLAG = "nesstarConvert";
    private static final String NESSTAR_ATTACHMENT_DOWNLOAD_FOLDER = "nesstarAttachmentDownloadFolder";
    private static final String NESSTAR_COMPENDIUM_MAPPING_FILE = "nesstarCompendiumMappingFile";

    private static final String HIPPO_IMPORT_DIR = "hippoImportDir";

    private static final String MIGRATION_REPORT_PATH = "migrationReport";

    private static final String TAXONOMY_DEFINITION_IMPORT_PATH = "taxonomyDefinitionImportPath";
    private static final String TAXONOMY_DEFINITION_OUTPUT_PATH = "taxonomyDefinitionOutputPath";
    private static final String TAXONOMY_MAPPING_IMPORT_PATH = "taxonomyMappingImportPath";

    private static final Path MIGRATOR_TEMP_DIR_PATH = Paths.get(System.getProperty("java.io.tmpdir"), "migrator");

    private static final String HIPPO_IMPORT_DIR_DEFAULT_NAME = "exim-import";
    private static final String NESSTAR_UNZIPPED_ARCHIVE_DIR_NAME_DEFAULT = "nesstar-export";
    private static final String ATTACHMENT_DOWNLOAD_DIR_NAME_DEFAULT = "attachments";
    private static final String REPORTS_DIR_NAME = "reports";
    private static final String MIGRATION_REPORT_FILENAME_DEFAULT = "Clinical Indicators Migration Report {TIMESTAMP}.xlsx";


    private final ExecutionParameters executionParameters;


    public ExecutionConfigurator(final ExecutionParameters executionParameters) {
        this.executionParameters = executionParameters;
    }


    public void initExecutionParameters(final ApplicationArguments args) {

        executionParameters.setNesstarUnzipForce(args.containsOption(NESSTAR_FORCE_UNZIP_FLAG));

        executionParameters.setNesstarZippedExportFile(getPathArg(args, NESSTAR_ZIP_FILE_PATH));

        executionParameters.setConvertNesstar(args.containsOption(NESSTAR_CONVERT_FLAG));

        executionParameters.setNesstarCompendiumMappingFile(getPathArg(args, NESSTAR_COMPENDIUM_MAPPING_FILE));

        executionParameters.setTaxonomyDefinitionImportPath(getPathArg(args, TAXONOMY_DEFINITION_IMPORT_PATH));

        executionParameters.setTaxonomyMappingImportPath(getPathArg(args, TAXONOMY_MAPPING_IMPORT_PATH));


        initNesstarUnzippedArchiveDir();
        initMigrationReportOutputPath(args);
        initHippoImportDir(args);
        initDownloadDir(args);
        initTaxonomyDefinitionOutputPath(args);
    }

    private void initHippoImportDir(final ApplicationArguments args) {
        Path pathArg = getPathArg(args, HIPPO_IMPORT_DIR);
        if (pathArg == null) {
            pathArg = Paths.get(MIGRATOR_TEMP_DIR_PATH.toString(), HIPPO_IMPORT_DIR_DEFAULT_NAME);
        }

        executionParameters.setHippoImportDir(pathArg);
    }

    private void initDownloadDir(final ApplicationArguments args) {
        Path pathArg = getPathArg(args, NESSTAR_ATTACHMENT_DOWNLOAD_FOLDER);
        if (pathArg == null) {
            pathArg = Paths.get(MIGRATOR_TEMP_DIR_PATH.toString(), ATTACHMENT_DOWNLOAD_DIR_NAME_DEFAULT);
        }

        executionParameters.setNesstarAttachmentDownloadDir(pathArg);
    }

    private void initTaxonomyDefinitionOutputPath(final ApplicationArguments args) {
        Path pathArg = getPathArg(args, TAXONOMY_DEFINITION_OUTPUT_PATH);
        if (pathArg == null) {
            pathArg = Paths.get(MIGRATOR_TEMP_DIR_PATH.toString(), HIPPO_IMPORT_DIR_DEFAULT_NAME);
        }

        executionParameters.setTaxonomyDefinitionOutputPath(pathArg);
    }

    private void initNesstarUnzippedArchiveDir() {

        final Path nesstarUnzippedArchiveDir = Paths.get(
            MIGRATOR_TEMP_DIR_PATH.toString(),
            NESSTAR_UNZIPPED_ARCHIVE_DIR_NAME_DEFAULT);

        executionParameters.setNesstarUnzippedExportDir(nesstarUnzippedArchiveDir);
    }

    private void initMigrationReportOutputPath(ApplicationArguments args) {
        Path pathArg = getPathArg(args, MIGRATION_REPORT_PATH);
        if (pathArg == null) {
            pathArg = Paths.get(MIGRATOR_TEMP_DIR_PATH.toString(), REPORTS_DIR_NAME, MIGRATION_REPORT_FILENAME_DEFAULT);
        }
        executionParameters.setMigrationReportFilePath(pathArg);
    }

    private Path getPathArg(final ApplicationArguments args, final String commandLineArgName) {
        return Optional.ofNullable(args.getOptionValues(commandLineArgName))
            .orElse(emptyList())
            .stream()
            .findFirst()
            .map(Paths::get)
            .orElse(null);
    }

    public List<Descriptor> getArgumentsDescriptors() {
        return asList(
            describe(
                NESSTAR_ZIP_FILE_PATH,
                "Path to the ZIP file containing data exported from Nesstar."
            ),
            describe(
                NESSTAR_ATTACHMENT_DOWNLOAD_FOLDER,
                "Directory where the migrator will download attachments into." +
                    " Optional - if not provided, one will be created in a temporary space." +
                    " NOTE that the files will not be downloaded if they exist in the folder already."
            ),
            describe(
                NESSTAR_COMPENDIUM_MAPPING_FILE,
                "File containing mapping of Clinical Indicators Compendium." +
                    " Required if --" + NESSTAR_CONVERT_FLAG + " is specified, optional otherwise."
            ),
            describe(
                HIPPO_IMPORT_DIR,
                "Directory where the migrator will generate import files for Hippo to read them from." +
                    " Optional - if not provided, one will be created in a temporary space." +
                    " NOTE that the directory is deleted and re-created on each run."
            ),
            describe(
                TAXONOMY_DEFINITION_IMPORT_PATH,
                "Path to the spreadsheet that contains the taxonomy definition we want to import into hippo." +
                    " Required if --" + NESSTAR_CONVERT_FLAG + " is specified, optional otherwise."
            ),
            describe(
                TAXONOMY_DEFINITION_OUTPUT_PATH,
                "Path to output the taxonomy definition JSON to import into hippo." +
                    " Optional - if not provided, one will be created in a temporary space."
            ),
            describe(
                TAXONOMY_MAPPING_IMPORT_PATH,
                "Path to taxonomy mapping spreadsheet used to map migrated datasets' P code to " +
                    "hippo taxonomy keys as imported from the taxonomy definition." +
                    " Required if --" + NESSTAR_CONVERT_FLAG + " is specified."
            )
        );
    }

    public List<Descriptor> getFlagsDescriptors() {
        return asList(
            describe(
                HELP_FLAG,
                "Displays usage info and quits."
            ),
            describe(
                NESSTAR_FORCE_UNZIP_FLAG,
                "By default, when Nesstar export directory exists, no attempt of extracting" +
                    " Nesstar ZIP file will be made. This flag forces deletion of original directory" +
                    " and decompressing the archive anew."
            ),
            describe(
                NESSTAR_CONVERT_FLAG,
                "Triggers conversion of Nesstar export to Hippo import format."
            )
        );
    }
}
