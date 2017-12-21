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

    public static final String NESSTAR_ZIP_FILE_PATH = "nesstarUnzipFrom";
    public static final String NESSTAR_FORCE_UNZIP_FLAG = "nesstarForceUnzip";
    public static final String NESSTAR_CONVERT_FLAG = "nesstarConvert";
    public static final String NESSTAR_ATTACHMENT_DOWNLOAD_FOLDER = "nesstarAttachmentDownloadFolder";
    public static final String NESSTAR_COMPENDIUM_MAPPING_FILE = "nesstarCompendiumMappingFile";

    public static final String HIPPO_IMPORT_DIR = "hippoImportDir";

    public static final String MIGRATION_REPORT_PATH = "migrationReport";

    public static final String TAXONOMY_SPREADSHEET_PATH = "taxonomySpreadsheetPath";
    public static final String TAXONOMY_OUTPUT_PATH = "taxonomyOutputPath";
    public static final String TAXONOMY_MAPPING_IMPORT_PATH = "taxonomyMappingImportPath";

    private static final String HIPPO_IMPORT_DIR_DEFAULT_NAME = "exim-import";
    private static final String NESSTAR_UNZIPPED_ARCHIVE_DIR_NAME_DEFAULT = "nesstar-export";
    private static final String ATTACHMENT_DOWNLOAD_DIR_NAME_DEFAULT = "attachments";
    private static final String TAXONOMY_OUTPUT_PATH_DEFAULT = "taxonomy";
    private static final String TAXONOMY_MAPPING_IMPORT_PATH_DEFAULT = "./repository-data/development/src/main/resources/hcm-content/content/taxonomies/publication_taxonomy.yaml";
    private static final String MIGRATION_REPORT_FILENAME_DEFAULT = "migration-report.txt";


    private final ExecutionParameters executionParameters;
    private static final Path TEMP_DIR_PATH = Paths.get(System.getProperty("java.io.tmpdir"));


    public ExecutionConfigurator(final ExecutionParameters executionParameters) {
        this.executionParameters = executionParameters;
    }


    public void initExecutionParameters(final ApplicationArguments args) {

        executionParameters.setNesstarUnzipForce(args.containsOption(NESSTAR_FORCE_UNZIP_FLAG));

        executionParameters.setNesstarZippedExportFile(getPathArg(args, NESSTAR_ZIP_FILE_PATH));

        executionParameters.setConvertNesstar(args.containsOption(NESSTAR_CONVERT_FLAG));

        executionParameters.setNesstarCompendiumMappingFile(getPathArg(args, NESSTAR_COMPENDIUM_MAPPING_FILE));

        executionParameters.setTaxonomySpreadsheetPath(getPathArg(args, TAXONOMY_SPREADSHEET_PATH));

        executionParameters.setTaxonomyMappingImportPath(getPathArg(args, TAXONOMY_MAPPING_IMPORT_PATH));

        initNesstarUnzippedArchiveDir();
        initMigrationReportOutputPath(args);
        initHippoImportDir(args);
        initDownloadDir(args);
        initTaxonomyOutputPath(args);
        initTaxonomyMappingImportPath(args);
    }

    public void initHippoImportDir(final ApplicationArguments args) {
        Path pathArg = getPathArg(args, HIPPO_IMPORT_DIR);
        if (pathArg == null) {
            pathArg = Paths.get(TEMP_DIR_PATH.toString(), HIPPO_IMPORT_DIR_DEFAULT_NAME);
        }

        executionParameters.setHippoImportDir(pathArg);
    }

    public void initDownloadDir(final ApplicationArguments args) {
        Path pathArg = getPathArg(args, NESSTAR_ATTACHMENT_DOWNLOAD_FOLDER);
        if (pathArg == null) {
            pathArg = Paths.get(TEMP_DIR_PATH.toString(), ATTACHMENT_DOWNLOAD_DIR_NAME_DEFAULT);
        }

        executionParameters.setNesstarAttachmentDownloadDir(pathArg);
    }

    private void initTaxonomyOutputPath(final ApplicationArguments args) {
        Path pathArg = getPathArg(args, TAXONOMY_OUTPUT_PATH);
        if (pathArg == null) {
            pathArg = Paths.get(TEMP_DIR_PATH.toString(), HIPPO_IMPORT_DIR_DEFAULT_NAME, TAXONOMY_OUTPUT_PATH_DEFAULT);
        }

        executionParameters.setTaxonomyOutputPath(pathArg);
    }

    private void initTaxonomyMappingImportPath(final ApplicationArguments args) {
        Path pathArg = getPathArg(args, TAXONOMY_MAPPING_IMPORT_PATH);
        if (pathArg == null) {
            pathArg = Paths.get(TAXONOMY_MAPPING_IMPORT_PATH_DEFAULT);
        }

        executionParameters.setTaxonomyMappingImportPath(pathArg);
    }

    private void initNesstarUnzippedArchiveDir() {

        final Path nesstarUnzippedArchiveDir = Paths.get(
            TEMP_DIR_PATH.toString(),
            NESSTAR_UNZIPPED_ARCHIVE_DIR_NAME_DEFAULT);

        executionParameters.setNesstarUnzippedExportDir(nesstarUnzippedArchiveDir);
    }

    private void initMigrationReportOutputPath(ApplicationArguments args) {
        Path pathArg = getPathArg(args, MIGRATION_REPORT_PATH);
        if (pathArg == null) {
            pathArg = Paths.get(TEMP_DIR_PATH.toString(), MIGRATION_REPORT_FILENAME_DEFAULT);
        }
        executionParameters.setMigrationReportOutputPath(pathArg);
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
                TAXONOMY_SPREADSHEET_PATH,
                "Path to the spreadsheet that contains the taxonomy we want to import into hippo." +
                    " Optional - if not provided, taxonomy will not be imported."
            ),
            describe(
                TAXONOMY_OUTPUT_PATH,
                "Path to output the taxonomy JSON to import into hippo." +
                    " Optional - if not provided, one will be created in a temporary space."
            ),
            describe(
                TAXONOMY_MAPPING_IMPORT_PATH,
                "Path to taxonomy yaml import file used for the mapping of keywords in the migrator process." +
                    " Optional - if not provided, the sample (partially populated) taxonomy file from" +
                    " the development module will used instead."
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
