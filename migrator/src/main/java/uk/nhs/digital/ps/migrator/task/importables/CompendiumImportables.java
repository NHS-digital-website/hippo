package uk.nhs.digital.ps.migrator.task.importables;

import static java.text.MessageFormat.format;
import static java.util.stream.StreamSupport.stream;
import static uk.nhs.digital.ps.migrator.report.IncidentType.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uk.nhs.digital.ps.migrator.config.ExecutionParameters;
import uk.nhs.digital.ps.migrator.model.hippo.*;
import uk.nhs.digital.ps.migrator.model.nesstar.DataSetRepository;
import uk.nhs.digital.ps.migrator.model.nesstar.PublishingPackage;
import uk.nhs.digital.ps.migrator.report.IncidentType;
import uk.nhs.digital.ps.migrator.report.MigrationReport;
import uk.nhs.digital.ps.migrator.task.NesstarImportableItemsFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompendiumImportables {

    private static final String P_CODE_MAPPING_SHEET_NAME = "Mapping of P codes";
    private static final Pattern P_CODE_REGEX = Pattern.compile("P\\d+");

    private final ExecutionParameters executionParameters;
    private final NesstarImportableItemsFactory factory;
    private final MigrationReport migrationReport;

    public CompendiumImportables(final ExecutionParameters executionParameters,
                                 final NesstarImportableItemsFactory nesstarImportableItemsFactory,
                                 final MigrationReport migrationReport) {

        this.executionParameters = executionParameters;
        this.factory = nesstarImportableItemsFactory;
        this.migrationReport = migrationReport;
    }

    public Collection<HippoImportableItem> create(final DataSetRepository dataSetRepository, final Folder ciRootFolder, final Set<String> deliberatelyIgnoredPCodes) {

        assertRequiredArgs(executionParameters.getNesstarCompendiumMappingFile());

        final List<HippoImportableItem> importableItems = new ArrayList<>();

        final ImportablePrototypes importablePrototypes = readMappingFromFile(deliberatelyIgnoredPCodes);


        // Target CMS structure:
        //
        // A)  Compendium of population health indicators             FOLDER
        // B)    Compendium cancer incidence, survival and screening  FOLDER
        // C)      Current                                            FOLDER
        // D)        content                                          SERIES
        // E)        Cancer Incidence                                 FOLDER
        // F)          content                                        PUBLICATION
        // G)          DataSet                                        DATASET
        // H)    Archive                                              FOLDER
        // I)      Cancer Incidence                                   FOLDER
        // J)        content                                          SERIES
        // K)        Dec 2016                                         PUBLICATION (to be created manually by editors)

        // A)
        final Folder compendiumRootFolder = factory.newFolder(ciRootFolder, "Compendium of population health indicators");
        importableItems.add(compendiumRootFolder);

        importablePrototypes.getSeriesPrototypes().forEach(seriesPrototype -> {

            // B)
            final Folder seriesRootFolder = factory.newFolder(compendiumRootFolder, seriesPrototype.getName());
            importableItems.add(seriesRootFolder);

            // C)
            final Folder seriesCurrentFolder = factory.newFolder(seriesRootFolder, "Current");
            importableItems.add(seriesCurrentFolder);

            // D)
            final Series series = factory.newSeries(
                seriesCurrentFolder,
                seriesRootFolder.getLocalizedName(),
                seriesRootFolder.getLocalizedName() + " summary");
            importableItems.add(series);

            seriesPrototype.getPublicationPrototypes().forEach(publicationPrototype -> {

                // E)
                final Folder publicationFolder = factory.newFolder(seriesCurrentFolder, publicationPrototype.getName());
                importableItems.add(publicationFolder);

                // G)
                ArrayList<HippoImportableItem> datasets = new ArrayList<>();
                publicationPrototype.getDatasetIds().forEach(datasetId -> {
                    final PublishingPackage publishingPackage = dataSetRepository.findById(datasetId);
                    if (publishingPackage == null) {
                        migrationReport.report(datasetId, IncidentType.DATASET_MISSING_IN_MAPPING, publicationPrototype.getName());
                    } else {
                        datasets.add(factory.toDataSet(publicationFolder, publishingPackage));
                    }
                });

                // F)
                final Publication publication = factory.newPublication(
                    publicationFolder,
                    "content",
                    publicationPrototype.getName(),
                    datasets);

                importableItems.add(publication);
                importableItems.addAll(datasets);
            });

            // H)
            final Folder archiveFolder = factory.newFolder(seriesRootFolder, "Archive");
            importableItems.add(archiveFolder);

            seriesPrototype.getPublicationPrototypes().forEach(publicationPrototype -> {

                // I) - what used to be publication in 'Current', turns into a series in 'Archive'
                final Folder seriesArchiveFolder = factory.newFolder(archiveFolder, publicationPrototype.getName());
                importableItems.add(seriesArchiveFolder);

                // J)
                final Archive archiveContent = factory.newArchive(seriesArchiveFolder, seriesArchiveFolder.getLocalizedName());
                importableItems.add(archiveContent);
            });

        });

        return importableItems;
    }

    private ImportablePrototypes readMappingFromFile(final Set<String> deliberatelyIgnoredPCodes) {

        final Sheet mappingOfPCodesSheet = loadCompendiumMappingSpreadsheet();

        identifyPCodesDeliberatelyIgnored(mappingOfPCodesSheet, deliberatelyIgnoredPCodes);

        reportOnPCodesWithNoMapping(mappingOfPCodesSheet);
        reportOnPCodesWithInvalidOrNoImportStatusMapping(mappingOfPCodesSheet);

        return streamRows(mappingOfPCodesSheet)
            .filter(this::shouldMigrate)

            .filter(row -> getPCode(row).isPresent())
            .filter(row -> getSeriesName(row).isPresent())
            .filter(row -> getPublicationName(row).isPresent())

            .map(row -> new String[]{
                getSeriesName(row).get(),
                getPublicationName(row).get(),
                getPCode(row).get()
            })
            .collect(
                () -> new ImportablePrototypes(migrationReport),
                (prototypes, components) -> prototypes.add(components[0], components[1], components[2]),
                (l, r) -> { /* no-op - no need for a combinator in serial stream */ }
            );
    }

    private boolean shouldMigrate(final Row row) {
        return "Y".equalsIgnoreCase(getShouldMigrateValue(row).orElse(null));
    }

    private Optional<String> getShouldMigrateValue(final Row row) {
        return getCellStringValue(row, 0);
    }

    private Optional<String> getPCode(final Row row) {
        return getCellStringValue(row, 4)
            .map(pCode -> {
                // some P-codes (dataset ids) in the spreadsheet were found with extra characters,
                // (likely copy-paste debris) we need to sanitise such values
                final Matcher pCodeMatcher = P_CODE_REGEX.matcher(pCode);

                return pCodeMatcher.find() ? pCodeMatcher.group() : null;
            });
    }

    private Optional<String> getSeriesName(final Row row) {
        return getCellStringValue(row, 2);
    }

    private Optional<String> getPublicationName(final Row row) {
        return getCellStringValue(row, 3);
    }

    private Optional<String> getCellStringValue(final Row row, final int cellOffset) {
        final Cell cell = row.getCell(cellOffset);
        return Optional.ofNullable(cell == null ? null : cell.getStringCellValue().trim());
    }

    private void reportOnPCodesWithNoMapping(final Sheet mappingOfPCodesSheet) {
        streamRows(mappingOfPCodesSheet)
            .filter(row -> getPCode(row).isPresent())
            .filter(row -> !getSeriesName(row).isPresent() || !getPublicationName(row).isPresent())
            .forEach(row ->
                migrationReport.report(getPCode(row).get(), PCODE_WITH_INVALID_PARENT,
                    format("{0}:{1}", getSeriesName(row).orElse(""), getPublicationName(row).orElse(""))
                ));
    }

    private void reportOnPCodesWithInvalidOrNoImportStatusMapping(final Sheet mappingOfPCodesSheet) {
        streamRows(mappingOfPCodesSheet)
            .filter(row -> getPCode(row).isPresent())
            .filter(row -> !getShouldMigrateValue(row).isPresent()
                || !("Y".equalsIgnoreCase(getShouldMigrateValue(row).get()) || "N".equalsIgnoreCase(getShouldMigrateValue(row).get()))
            )
            .forEach(row ->
                migrationReport.report(getPCode(row).get(), PCODE_IMPORT_STATUS_MISSING, getShouldMigrateValue(row).orElse(""))
            );
    }

    private void identifyPCodesDeliberatelyIgnored(final Sheet mappingOfPCodesSheet,
                                                   final Set<String> deliberatelyIgnoredPCodes) {

        deliberatelyIgnoredPCodes.addAll(
            streamRows(mappingOfPCodesSheet)
                .filter(row -> getPCode(row).isPresent())
                .filter(row -> getShouldMigrateValue(row).isPresent())
                .filter(row -> !shouldMigrate(row))
                .map(row -> getPCode(row).get())
                .collect(Collectors.toSet())
        );
    }

    private Sheet loadCompendiumMappingSpreadsheet() {
        Workbook workbook;
        try {
            final InputStream excelFile = new FileInputStream(
                executionParameters.getNesstarCompendiumMappingFile().toFile()
            );

            workbook = new XSSFWorkbook(excelFile);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read Clinical Indicators Compendium mapping from file " + executionParameters.getNesstarCompendiumMappingFile(), e);

        }

        final Sheet sheet = workbook.getSheet(P_CODE_MAPPING_SHEET_NAME);

        if (sheet == null) {
            throw new RuntimeException(format(
                "Failed to read Clinical Indicators Compendium mapping; sheet ''{0}'' not found.", P_CODE_MAPPING_SHEET_NAME
            ));
        }

        return sheet;
    }

    private Stream<Row> streamRows(final Sheet xssfSheet) {
        return stream(((Iterable<Row>) xssfSheet::rowIterator).spliterator(), false);
    }

    private void assertRequiredArgs(final Path nesstarMappingFile) {

        if (nesstarMappingFile == null) {
            throw new IllegalArgumentException(
                "Required path to Clinical Indicators Compendium mapping file was not specified."
            );
        }

        if (!Files.isRegularFile(nesstarMappingFile)) {
            throw new IllegalArgumentException(
                "Nesstar mapping file does not exist: " + nesstarMappingFile
            );
        }
    }

    static class PublicationPrototype {

        private final MigrationReport migrationReport;

        private final String name;
        private final Set<String> datasets = new HashSet<>();

        public PublicationPrototype(final MigrationReport migrationReport,
                                    final String name) {
            this.migrationReport = migrationReport;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void add(final String datasetId) {
            if (!datasets.add(datasetId)) {
                migrationReport.report(datasetId, DUPLICATE_PCODE_WITHIN_PUB, name);
            }
        }

        public Set<String> getDatasetIds() {
            return datasets;
        }
    }

    static class SeriesPrototype {
        private final MigrationReport migrationReport;

        private String name;
        private Map<String, PublicationPrototype> publications = new HashMap<>();

        public SeriesPrototype(final MigrationReport migrationReport,
                               final String name) {
            this.migrationReport = migrationReport;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void add(final String publicationName, final String datasetId) {
            if (!publications.containsKey(publicationName)) {
                publications.put(publicationName, new PublicationPrototype(migrationReport, publicationName));
            }

            publications.get(publicationName).add(datasetId);
        }

        public Collection<PublicationPrototype> getPublicationPrototypes() {
            return publications.values();
        }
    }

    static class ImportablePrototypes {

        private final MigrationReport migrationReport;

        private Map<String, SeriesPrototype> series = new HashMap<>();

        ImportablePrototypes(final MigrationReport migrationReport) {
            this.migrationReport = migrationReport;
        }

        public void add(final String seriesName, final String publicationName, final String datasetId) {

            if (!series.containsKey(seriesName)) {
                series.put(seriesName, new SeriesPrototype(migrationReport, seriesName));
            }

            series.get(seriesName).add(publicationName, datasetId);
        }

        public List<SeriesPrototype> getSeriesPrototypes() {
            return new ArrayList<>(series.values());
        }
    }

}
