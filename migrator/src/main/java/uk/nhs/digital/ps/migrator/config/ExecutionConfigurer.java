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

public class ExecutionConfigurer {

    public static final String HELP_FLAG = "help";

    // rktodo better names (enum?)
    public static final String NESSTAR_ZIP_FILE_PATH = "nesstarUnzipFrom";
    public static final String NESSTAR_FORCE_UNZIP_FLAG = "nesstarForceUnzip";
    public static final String NESSTAR_CONVERT_FLAG = "nesstarConvert";

    public static final String HIPPO_IMPORT_DIR = "hippoImportDir";

    private static final String HIPPO_IMPORT_DIR_DEFAULT_NAME = "exim-import";
    private static final String NESSTAR_UNZIPPED_ARCHIVE_DIR_NAME_DEFAULT = "nesstar-export";


    private final ExecutionParameters executionParameters;
    private static final Path TEMP_DIR_PATH = Paths.get(System.getProperty("java.io.tmpdir"));


    public ExecutionConfigurer(final ExecutionParameters executionParameters) {
        this.executionParameters = executionParameters;
    }


    public void initExecutionParameters(final ApplicationArguments args) {

        executionParameters.setIsNesstarUnzipForce(args.containsOption(NESSTAR_FORCE_UNZIP_FLAG));

        executionParameters.setNesstarZippedExportFile(getPathArg(args, NESSTAR_ZIP_FILE_PATH));

        executionParameters.setConvertNesstar(args.containsOption(NESSTAR_CONVERT_FLAG));

        initNesstarUnzippedArchiveDir();

        initHippoImportDir(args);
    }

    public void initHippoImportDir(final ApplicationArguments args) {
        Path pathArg = getPathArg(args, HIPPO_IMPORT_DIR);
        if (pathArg == null) {
            pathArg = Paths.get(TEMP_DIR_PATH.toString(), HIPPO_IMPORT_DIR_DEFAULT_NAME);
        }

        executionParameters.setHippoImportDir(pathArg);
    }

    private void initNesstarUnzippedArchiveDir() {

        final Path nesstarUnzippedArchiveDir = Paths.get(
            TEMP_DIR_PATH.toString(),
            NESSTAR_UNZIPPED_ARCHIVE_DIR_NAME_DEFAULT);

        executionParameters.setNesstarUnzippedExportDir(nesstarUnzippedArchiveDir);
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
            ),
            describe(
                HIPPO_IMPORT_DIR,
                "Directory where the migrator will generate import files for Hippo to read them from." +
                    " Optional - if not provided, one will be created in a temporary space." +
                    " NOTE that the directory is deleted and re-created on each run."
            )
        );
    }
}
