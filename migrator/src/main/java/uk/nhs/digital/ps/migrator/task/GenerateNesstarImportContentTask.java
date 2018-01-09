package uk.nhs.digital.ps.migrator.task;

import org.apache.commons.io.FileUtils;
import uk.nhs.digital.ps.migrator.config.ExecutionParameters;
import uk.nhs.digital.ps.migrator.model.hippo.DataSet;
import uk.nhs.digital.ps.migrator.model.hippo.Folder;
import uk.nhs.digital.ps.migrator.model.hippo.HippoImportableItem;
import uk.nhs.digital.ps.migrator.model.hippo.TaxonomyMigrator;
import uk.nhs.digital.ps.migrator.model.nesstar.Catalog;
import uk.nhs.digital.ps.migrator.model.nesstar.CatalogStructure;
import uk.nhs.digital.ps.migrator.model.nesstar.DataSetRepository;
import uk.nhs.digital.ps.migrator.model.nesstar.PublishingPackage;
import uk.nhs.digital.ps.migrator.report.MigrationReport;
import uk.nhs.digital.ps.migrator.task.importables.CcgImportables;
import uk.nhs.digital.ps.migrator.task.importables.CompendiumImportables;
import uk.nhs.digital.ps.migrator.task.importables.NhsOutcomesFrameworkImportables;
import uk.nhs.digital.ps.migrator.task.importables.SocialCareImportables;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;
import static uk.nhs.digital.ps.migrator.misc.XmlHelper.loadFromXml;
import static uk.nhs.digital.ps.migrator.report.IncidentType.DUPLICATE_PCODE_IMPORTED;
import static uk.nhs.digital.ps.migrator.report.IncidentType.NO_DATASET_MAPPING;

public class GenerateNesstarImportContentTask implements MigrationTask {

    private static final String PUBLISHING_PACKAGES_DIR_NAME = "PublishingPackages";
    private static final String NESSTAR_BUNDLE_DIR_NAME = "NesstarBundle";
    private static final String NESSTAR_STRUCTURE_FILE_NAME = "structure.rdf";
    private static final String ROOT_CATALOG_LABEL = "NHS Digital indicators";


    private final ExecutionParameters executionParameters;

    private final ImportableItemsFactory importableItemsFactory;
    private final SocialCareImportables socialCareImportables;
    private final CcgImportables ccgImportables;
    private final NhsOutcomesFrameworkImportables nhsOutcomesFrameworkImportables;
    private final CompendiumImportables compendiumImportables;
    private final ImportableFileWriter importableFileWriter;
    private final MigrationReport migrationReport;
    private final TaxonomyMigrator taxonomyMigrator;


    public GenerateNesstarImportContentTask(final ExecutionParameters executionParameters,
                                            final ImportableItemsFactory importableItemsFactory,
                                            final SocialCareImportables socialCareImportables,
                                            final CcgImportables ccgImportables,
                                            final NhsOutcomesFrameworkImportables nhsOutcomesFrameworkImportables,
                                            final CompendiumImportables compendiumImportables,
                                            final ImportableFileWriter importableFileWriter,
                                            final MigrationReport migrationReport,
                                            final TaxonomyMigrator taxonomyMigrator) {

        this.executionParameters = executionParameters;
        this.importableItemsFactory = importableItemsFactory;
        this.socialCareImportables = socialCareImportables;
        this.ccgImportables = ccgImportables;
        this.nhsOutcomesFrameworkImportables = nhsOutcomesFrameworkImportables;
        this.compendiumImportables = compendiumImportables;
        this.importableFileWriter = importableFileWriter;
        this.migrationReport = migrationReport;
        this.taxonomyMigrator = taxonomyMigrator;
    }

    @Override
    public boolean isRequested() {
        return executionParameters.isConvertNesstar();
    }

    @Override
    public void execute() {

        final Path hippoImportDir = executionParameters.getHippoImportDir();
        final Path nesstarUnzippedArchiveLocation = executionParameters.getNesstarUnzippedExportDir();
        final Path taxonomyDefinitionImportPath = executionParameters.getTaxonomyDefinitionImportPath();
        final Path taxonomyMappingImportPath = executionParameters.getTaxonomyMappingImportPath();

        final Path publishingPackagesDir = Paths.get(nesstarUnzippedArchiveLocation.toString(),
            PUBLISHING_PACKAGES_DIR_NAME);

        final Path nesstarBundleDir = Paths.get(nesstarUnzippedArchiveLocation.toString(), NESSTAR_BUNDLE_DIR_NAME);
        final Path nesstarStructureFile = Paths.get(nesstarBundleDir.toString(), NESSTAR_STRUCTURE_FILE_NAME);

        try {
            assertRequiredArgs(hippoImportDir, nesstarUnzippedArchiveLocation, taxonomyDefinitionImportPath, taxonomyMappingImportPath);

            taxonomyMigrator.init();

            final DataSetRepository dataSetRepository = loadDataSetExportedModels(publishingPackagesDir);

            final CatalogStructure catalogStructure = loadFromXml(nesstarStructureFile, CatalogStructure.class);
            catalogStructure.setDataSetRepository(dataSetRepository);

            final Set<String> deliberatelyIgnoredPCodes = new HashSet<>();

            final List<HippoImportableItem> importableItems = createImportableItemsModels(
                catalogStructure,
                dataSetRepository,
                deliberatelyIgnoredPCodes
            );

            updateMigrationReport(dataSetRepository, catalogStructure, deliberatelyIgnoredPCodes, importableItems);

            recreate(hippoImportDir);

            importableFileWriter.writeImportableFiles(importableItems, hippoImportDir);

        } catch (final Exception e) {
            throw new RuntimeException("Failed to convert Nesstar export.", e);
        }
    }

    private void updateMigrationReport(final DataSetRepository dataSetRepository,
                                       final CatalogStructure catalogStructure,
                                       final Set<String> deliberatelyIgnoredPCodes,
                                       final List<HippoImportableItem> importableItems
    ) {
        migrationReport.setDatasetRepository(dataSetRepository);

        identifyPCodesFromIgnoredSections(catalogStructure, deliberatelyIgnoredPCodes);

        reportDuplicateDatasets(importableItems);

        reportMissedDatasets(dataSetRepository, importableItems, deliberatelyIgnoredPCodes);
    }

    private void reportDuplicateDatasets(final List<HippoImportableItem> importableItems) {

        final Set<String> visitedPcodes = new HashSet<>();

        importableItems.stream()
            .filter(DataSet.class::isInstance)
            .map(hippoImportableItem -> (DataSet) hippoImportableItem)
            .map(DataSet::getPCode)
            .filter(pCode -> !visitedPcodes.add(pCode))
            .forEach(pCode -> migrationReport.report(pCode, DUPLICATE_PCODE_IMPORTED));
    }

    private void reportMissedDatasets(final DataSetRepository dataSetRepository,
                                      final List<HippoImportableItem> importableItems,
                                      final Set<String> deliberatelyIgnoredPCodes) {

        final Set<String> pCodes = new HashSet<>(); // to avoid exceptions coming from duplicate entries

        final Map<String, DataSet> datasetsByUniqueIdentifier = importableItems.stream()
            .filter(DataSet.class::isInstance)
            .map(hippoImportableItem -> (DataSet) hippoImportableItem)
            .filter(dataSet -> !pCodes.contains(dataSet.getPCode()))
            .peek(dataSet -> pCodes.add(dataSet.getPCode())) // to avoid exceptions coming from duplicate entries
            .collect(toMap(DataSet::getPCode, dataSet -> dataSet));

        dataSetRepository.stream()
            .filter(dataSet -> !deliberatelyIgnoredPCodes.contains(dataSet.getUniqueIdentifier()))
            .filter(publishingPackage -> !datasetsByUniqueIdentifier.containsKey(publishingPackage.getUniqueIdentifier()))
            .forEach(publishingPackage ->
                migrationReport.report(publishingPackage.getUniqueIdentifier(), NO_DATASET_MAPPING)
            );
    }

    private List<HippoImportableItem> createImportableItemsModels(final CatalogStructure catalogStructure,
                                                                  final DataSetRepository datasetRepository,
                                                                  final Set<String> deliberatelyIgnoredPCodes) {

        List<HippoImportableItem> importableItems = new ArrayList<>();

        // Create Clinical Indicators folder as a root folder for all clinical indicators,
        // separating them from Statistical Publications.
        // Expected CMS path: Corporate Website/Publications System/Clinical Indicators

        final Folder rootClinicalIndicatorsFolder = importableItemsFactory.toFolder(
            null, catalogStructure.findCatalogByLabel(ROOT_CATALOG_LABEL)
        );
        rootClinicalIndicatorsFolder.setLocalizedName("Clinical Indicators");
        rootClinicalIndicatorsFolder.setJcrNodeName("clinical-indicators");

        importableItems.add(rootClinicalIndicatorsFolder);

        // Create individual sub-sections of Clinical Indicators

        importableItems.addAll(
            ccgImportables.create(catalogStructure, rootClinicalIndicatorsFolder)
        );
        importableItems.addAll(
            socialCareImportables.create(catalogStructure, rootClinicalIndicatorsFolder)
        );
        importableItems.addAll(
            nhsOutcomesFrameworkImportables.create(catalogStructure, rootClinicalIndicatorsFolder)
        );
        importableItems.addAll(
            compendiumImportables.create(datasetRepository, rootClinicalIndicatorsFolder, deliberatelyIgnoredPCodes)
        );

        // If we had any errors we will have nulls in the list, we have logged and output the errors already so just strip the nulls
        importableItems = importableItems.stream()
            .filter(Objects::nonNull)
            .collect(toList());

        return importableItems;
    }

    private void identifyPCodesFromIgnoredSections(final CatalogStructure catalogStructure,
                                                   final Set<String> deliberatelyIgnoredPCodes
    ) {

        final List<String> ignoredTopLevelSectionsLabels = asList(
            "Indicator Portal news",
            "Patient Online Services",
            "Quality Accounts",
            "Summary Hospital-level Mortality Indicator (SHMI)",
            "Archive",
            "Contact us"
        );

        final List<Catalog> topLevelCatalogs = catalogStructure.findCatalogByLabel(ROOT_CATALOG_LABEL)
            .getChildCatalogs();

        final List<Catalog> topLevelCatalogsToIgnore = ignoredTopLevelSectionsLabels.stream()
            .map(catalogStructure::findCatalogByLabel)
            // ensures that we only find the top level catalogs, in case any of the labels
            // would need deeper-nested catalogs (unlikely as it is)
            .filter(topLevelCatalogs::contains)
            .collect(toList());

        final Set<String> pCodesToIgnore = topLevelCatalogsToIgnore.stream()
            .flatMap(catalog -> catalog.getAllDescendantCatalogs().stream())
            .flatMap(catalog -> catalog.findPublishingPackages().stream())
            .map(PublishingPackage::getUniqueIdentifier)
            .collect(toSet());

        deliberatelyIgnoredPCodes.addAll(pCodesToIgnore);
    }

    private DataSetRepository loadDataSetExportedModels(final Path publishingPackagesDir) {
        return new DataSetRepository(findDataSetFiles(publishingPackagesDir)
            .map(path -> loadFromXml(path, PublishingPackage.class))
            .collect(toList())
        );
    }

    /**
     * @return XML files representing individual data sets (XML files under named following pattern P00000.xml)
     */
    private Stream<Path> findDataSetFiles(final Path publishingPackagesDir) {
        try {
            return Files.find(
                publishingPackagesDir,
                Integer.MAX_VALUE,
                (path, basicFileAttributes) -> basicFileAttributes.isRegularFile()
                    && path.getFileName().toString().matches("P\\d+.xml")
            );

        } catch (final IOException e) {
            throw new UncheckedIOException("Failed to find codeBook files.", e);
        }
    }


    private void assertRequiredArgs(final Path hippoImportDir,
                                    final Path nesstarUnzippedArchiveLocation,
                                    final Path taxonomyDefinitionImportPath,
                                    final Path taxonomyMappingImportPath) {

        if (hippoImportDir == null) {
            throw new IllegalArgumentException("Required Hippo import dir location was not specified.");
        }

        if (nesstarUnzippedArchiveLocation == null) {
            throw new IllegalArgumentException("Required Nesstar unzipped Archive location was not specified.");
        }

        if (!Files.exists(nesstarUnzippedArchiveLocation)) {
            throw new IllegalArgumentException(
                "Nesstar unzipped Archive location does not exist: " + nesstarUnzippedArchiveLocation
            );
        }

        if (taxonomyMappingImportPath == null) {
            throw new IllegalArgumentException("Required Taxonomy Mapping Import Path was not specified.");
        }

        if (!Files.isRegularFile(taxonomyMappingImportPath)) {
            throw new IllegalArgumentException(
                "Taxonomy Mapping Import file does not exist: " + taxonomyMappingImportPath
            );
        }

        if (taxonomyDefinitionImportPath == null) {
            throw new IllegalArgumentException("Required Taxonomy Definition Import Path was not specified.");
        }

        if (!Files.isRegularFile(taxonomyDefinitionImportPath)) {
            throw new IllegalArgumentException(
                "Taxonomy Definition Import file does not exist: " + taxonomyDefinitionImportPath
            );
        }
    }

    private void recreate(final Path dir) {
        try {
            FileUtils.deleteDirectory(dir.toFile());
            Files.createDirectories(dir);

        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
