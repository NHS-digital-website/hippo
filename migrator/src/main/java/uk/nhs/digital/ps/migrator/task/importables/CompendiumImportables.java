package uk.nhs.digital.ps.migrator.task.importables;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import uk.nhs.digital.ps.migrator.MigrationReport;
import uk.nhs.digital.ps.migrator.config.ExecutionParameters;
import uk.nhs.digital.ps.migrator.model.hippo.Folder;
import uk.nhs.digital.ps.migrator.model.hippo.HippoImportableItem;
import uk.nhs.digital.ps.migrator.model.hippo.Publication;
import uk.nhs.digital.ps.migrator.model.hippo.Series;
import uk.nhs.digital.ps.migrator.model.nesstar.DataSetRepository;
import uk.nhs.digital.ps.migrator.model.nesstar.PublishingPackage;
import uk.nhs.digital.ps.migrator.task.ImportableItemsFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.text.MessageFormat.format;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.slf4j.LoggerFactory.getLogger;

public class CompendiumImportables {

    private final static Logger log = getLogger(CompendiumImportables.class);

    private final ExecutionParameters executionParameters;
    private final ImportableItemsFactory factory;

    private static final Pattern P_CODE_REGEX = Pattern.compile("P\\d+");

    public CompendiumImportables(final ExecutionParameters executionParameters,
                                 final ImportableItemsFactory importableItemsFactory) {

        this.executionParameters = executionParameters;
        this.factory = importableItemsFactory;
    }

    public Collection<HippoImportableItem> create(final DataSetRepository dataSetRepository, final Folder ciRootFolder) {

        assertRequiredArgs(executionParameters.getNesstarCompendiumMappingFile());

        final List<HippoImportableItem> importableItems = new ArrayList<>();

        final ImportablePrototypes importablePrototypes = readMapping();


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
            final Folder seriesRootFolder = factory.newFolder(compendiumRootFolder, "Compendium " + seriesPrototype.getName());
            importableItems.add(seriesRootFolder);

            // C)
            final Folder seriesCurrentFolder = factory.newFolder(seriesRootFolder, "Current");
            importableItems.add(seriesCurrentFolder);

            // D)
            final Series series = factory.newSeries(seriesCurrentFolder, seriesRootFolder.getLocalizedName());
            importableItems.add(series);

            seriesPrototype.getPublicationPrototypes().forEach(publicationPrototype -> {

                // E)
                final Folder publicationFolder = factory.newFolder(seriesCurrentFolder, publicationPrototype.getName());
                importableItems.add(publicationFolder);

                // F)
                final Publication publication = factory.newPublication(
                    publicationFolder,
                    "content",
                    publicationPrototype.getName()
                );
                importableItems.add(publication);

                // G)
                publicationPrototype.getDatasetIds().forEach(datasetId -> {
                    final PublishingPackage publishingPackage = dataSetRepository.findById(datasetId);
                    if (publishingPackage == null) {
                        MigrationReport.add(null, "No dataset found with id:", datasetId,
                            "The following publication will be missing this dataset: " + publicationPrototype.getName());
                    } else {
                        importableItems.add(factory.toDataSet(publicationFolder, publishingPackage));
                    }
                });

            });

            // H)
            final Folder archiveFolder = factory.newFolder(seriesRootFolder, "Archive");
            importableItems.add(archiveFolder);

            seriesPrototype.getPublicationPrototypes().forEach(publicationPrototype -> {

                // I) - what used to be publication in 'Current', turns into a series in 'Archive'
                final Folder seriesArchiveFolder = factory.newFolder(archiveFolder, publicationPrototype.getName());
                importableItems.add(seriesArchiveFolder);

                // J)
                final Series seriesArchivedContent = factory.newSeries(seriesArchiveFolder, "Archived " + seriesArchiveFolder.getLocalizedName());
                importableItems.add(seriesArchivedContent);
            });

        });

        return importableItems;
    }

    private ImportablePrototypes readMapping() { // todo name

        XSSFWorkbook workbook;
        try {
            final InputStream excelFile = new FileInputStream(
                executionParameters.getNesstarCompendiumMappingFile().toFile()
            );

            workbook = new XSSFWorkbook(excelFile);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read Clinical Indicators Compendium mapping from file " + executionParameters.getNesstarCompendiumMappingFile(), e);
        }

        final XSSFSheet sheet3 = workbook.getSheet("Sheet3");

        streamRows(sheet3)
            .filter(row -> row.getCell(3) != null)
            .filter(row -> row.getCell(3).getStringCellValue().trim().matches("P\\d+.+"))

            .filter(row -> row.getCell(1) == null || isBlank(row.getCell(1).getStringCellValue()))
            .filter(row -> row.getCell(2) == null || isBlank(row.getCell(2).getStringCellValue()))

            .forEach(row ->
                MigrationReport.add(null,
                    format("Found P-code without matching series or publication definition: {0}:{1}:{2}",
                        row.getCell(1) == null ? "" : row.getCell(1).getStringCellValue(),
                        row.getCell(2) == null ? "" : row.getCell(2).getStringCellValue(),
                        row.getCell(3) == null ? "" : row.getCell(3).getStringCellValue()
                    ),
                    "Dataset will not be imported"
                )
            );

        return streamRows(sheet3)
            // only use rows that actually have a dataset indicator (the 'P-value')
            .filter(row -> row.getCell(3) != null)
            .filter(row -> row.getCell(3).getStringCellValue().trim().matches("P\\d+\\D*"))
            // only use rows where mapping is defined for a dataset
            // (reject rows with P-value but without Series or Publication values)
            .filter(row -> !(row.getCell(1) == null || isBlank(row.getCell(1).getStringCellValue())))
            .filter(row -> !(row.getCell(2) == null || isBlank(row.getCell(2).getStringCellValue())))
            .map(row -> {
                final String seriesName = row.getCell(1).getStringCellValue().trim();
                final String publicationName = row.getCell(2).getStringCellValue().trim();

                // some P-codes (dataset ids) in the spreadsheet were found with extra characters,
                // (likely copy-paste debris) we need to sanitise such values
                final Matcher pCodeMatcher = P_CODE_REGEX.matcher(
                    row.getCell(3).getStringCellValue().trim()
                );
                pCodeMatcher.find();
                final String datasetId = pCodeMatcher.group();

                return new String[]{seriesName, publicationName, datasetId};
            })
            .collect(
                ImportablePrototypes::new,
                (prototypes, row) -> prototypes.add(row[0], row[1], row[2]),
                (l, r) -> { /* no-op - no need for a combinator in serial stream */ }
            );
    }

    private Stream<Row> streamRows(final XSSFSheet sheet3) {
        return stream(((Iterable<Row>) sheet3::rowIterator).spliterator(), false);
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
        private String name;
        private Set<String> datasets = new HashSet<>();

        public PublicationPrototype(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void add(final String datasetId) {
            if (!datasets.add(datasetId)) {
                MigrationReport.add(null,
                    "Duplicate dataset added to publication: " + name,
                    "The dataset will only be imported once. ID: " + datasetId);
            }
        }

        public Set<String> getDatasetIds() {
            return datasets;
        }
    }

    static class SeriesPrototype {
        private String name;
        private Map<String, PublicationPrototype> publications = new HashMap<>();

        public SeriesPrototype(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void add(final String publicationName, final String datasetId) {
            if (!publications.containsKey(publicationName)) {
                publications.put(publicationName, new PublicationPrototype(publicationName));
            }

            publications.get(publicationName).add(datasetId);
        }

        public Collection<PublicationPrototype> getPublicationPrototypes() {
            return publications.values();
        }
    }

    static class ImportablePrototypes {

        private Map<String, SeriesPrototype> series = new HashMap<>();

        public void add(final String seriesName, final String publicationName, final String datasetId) {

            if (!series.containsKey(seriesName)) {
                series.put(seriesName, new SeriesPrototype(seriesName));
            }

            series.get(seriesName).add(publicationName, datasetId);
        }

        public List<SeriesPrototype> getSeriesPrototypes() {
            return new ArrayList<>(series.values());
        }
    }

}
