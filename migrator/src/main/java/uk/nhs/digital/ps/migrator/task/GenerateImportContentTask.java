package uk.nhs.digital.ps.migrator.task;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import uk.nhs.digital.ps.migrator.config.ExecutionParameters;
import uk.nhs.digital.ps.migrator.model.nesstar.*;
import uk.nhs.digital.ps.migrator.model.hippo.Folder;
import uk.nhs.digital.ps.migrator.model.hippo.HippoImportableItem;
import uk.nhs.digital.ps.migrator.model.hippo.Series;
import uk.nhs.digital.ps.migrator.model.hippo.Publication;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.ps.migrator.misc.XmlHelper.loadFromXml;
import static uk.nhs.digital.ps.migrator.task.ImportableFileWriter.writeImportableFiles;
import static uk.nhs.digital.ps.migrator.task.ImportableItemsFactory.*;

public class GenerateImportContentTask implements Task {

    private static final String PUBLISHING_PACKAGES_DIR_NAME = "PublishingPackages";
    private static final String NESSTAR_BUNDLE_DIR_NAME = "NesstarBundle";
    private static final String NESSTAR_STRUCTURE_FILE_NAME = "structure.rdf";


    private final Logger log = getLogger(getClass());

    private final ExecutionParameters executionParameters;

    public GenerateImportContentTask(final ExecutionParameters executionParameters) {
        this.executionParameters = executionParameters;
    }

    @Override
    public boolean isRequested() {
        return executionParameters.isConvertNesstar();
    }

    @Override
    public void execute() {

        final Path hippoImportDir = executionParameters.getHippoImportDir();

        final Path nesstarUnzippedArchiveLocation = executionParameters.getNesstarUnzippedExportDir();

        final Path publishingPackagesDir = Paths.get(nesstarUnzippedArchiveLocation.toString(),
            PUBLISHING_PACKAGES_DIR_NAME);


        final Path nesstarBundleDir = Paths.get(nesstarUnzippedArchiveLocation.toString(), NESSTAR_BUNDLE_DIR_NAME);
        final Path nesstarStructureFile = Paths.get(nesstarBundleDir.toString(), NESSTAR_STRUCTURE_FILE_NAME);

        try {
            assertRequiredArgs(hippoImportDir, nesstarUnzippedArchiveLocation);

            final DataSetRepository dataSetRepository = loadDataSetExportedModels(publishingPackagesDir);

            final CatalogStructure catalogStructure = loadFromXml(nesstarStructureFile, CatalogStructure.class);
            catalogStructure.setDataSetRepository(dataSetRepository);

            final List<HippoImportableItem> importableItems = createImportableItemsModels(catalogStructure);

            recreate(hippoImportDir);

            writeImportableFiles(importableItems, hippoImportDir);

        } catch (final Exception e) {
            throw new RuntimeException("Failed to convert Nesstar export.", e);
        }
    }

    private List<HippoImportableItem> createImportableItemsModels(final CatalogStructure catalogStructure) {

        final List<HippoImportableItem> importableItems = new ArrayList<>();

        // Create Clinical Indicators folder as a root folder for all clinical indicators,
        // separating them from Statistical Publications.
        // Expected CMS path: Corporate Website/Publications System/Clinical Indicators

        final Folder rootClinicalIndicatorsFolder =
            toFolder(catalogStructure.findCatalogByLabel("NHS Digital indicators"), null);
        rootClinicalIndicatorsFolder.setLocalizedName("Clinical Indicators");
        rootClinicalIndicatorsFolder.setJcrNodeName("clinical-indicators");

        importableItems.add(rootClinicalIndicatorsFolder);

        // Create individual sub-sections of Clinical Indicators

        importableItems.addAll(
            createCcgImportables(catalogStructure, rootClinicalIndicatorsFolder)
        );
        importableItems.addAll(
            createSocialCareImportables(catalogStructure, rootClinicalIndicatorsFolder)
        );
        importableItems.addAll(
            createNhsOutcomesFrameworkImportables(catalogStructure, rootClinicalIndicatorsFolder)
        );

        return importableItems;
    }

    private DataSetRepository loadDataSetExportedModels(final Path publishingPackagesDir) {
        return new DataSetRepository(findDataSetFiles(publishingPackagesDir)
            .peek(path -> log.debug("Loading from {}", path))
            .map(path -> loadFromXml(path, PublishingPackage.class))
            .peek(dataSet -> log.debug("Loaded {}", dataSet))
            .collect(toList())
        );
    }

    private List<HippoImportableItem> createCcgImportables(final CatalogStructure catalogStructure,
                                                           final Folder ciRootFolder) {

        // Target CMS structure:
        //
        // *  Clinical Indicators folder                     one
        // A)   CCG root folder                              one
        // B)   CCG series 'content' document                one
        // C)     Quarterly publication folder               one
        // D)     Quarterly publication 'content' document   one
        // E)       Domain X folders                         one per each domain
        // F)         DataSet X documents                    one per each dataset within each domain

        // A)
        final Catalog ccgRootCatalog = catalogStructure.findCatalogByLabel("CCG Outcomes Indicator Set");

        final Folder ccgRootFolder = toFolder(ccgRootCatalog, ciRootFolder);

        // B)
        final Series ccgSeries = toSeries(ccgRootCatalog, ccgRootFolder);

        // C)
        final String currentQuarterFolderName = "2017 Dec"; // rktodo dynamic? fixed?
        final Folder quarterlyPublicationFolder = newFolder(currentQuarterFolderName, ccgRootFolder);

        // D)
        final Publication quarterlyPublication = newPublication("content", currentQuarterFolderName, quarterlyPublicationFolder);
        quarterlyPublication.setLocalizedName("content");

        // e)
        // There is only one level of Domains under CCG so it's enough to just iterate over them rather than walk a tree
        final List<Catalog> domainCatalogs = ccgRootCatalog.getChildCatalogs();

        final List<HippoImportableItem> domainsWithDatasets = domainCatalogs.stream()
            .flatMap(domainCatalog -> {
                final Folder domainFolder = toFolder(domainCatalog, quarterlyPublicationFolder);

                return Stream.concat(
                    Stream.of(domainFolder),
                    // F)
                    domainCatalog.findPublishingPackages().stream().map(domainPublishingPackage ->
                            toDataSet(domainPublishingPackage, domainFolder)
                    )
                );

            }).collect(toList());

        final List<HippoImportableItem> importableItems = new ArrayList<>();
        importableItems.add(ccgRootFolder);
        importableItems.add(ccgSeries);
        importableItems.add(quarterlyPublicationFolder);
        importableItems.add(quarterlyPublication);
        importableItems.addAll(domainsWithDatasets);

        return importableItems;
    }

    private Collection<HippoImportableItem> createNhsOutcomesFrameworkImportables(final CatalogStructure catalogStructure,
                                                                                  final Folder ciRootFolder) {
        return emptyList(); // rktodo
    }

    private List<HippoImportableItem> createSocialCareImportables(final CatalogStructure catalogStructure,
                                                                  final Folder ciRootFolder) {
        return emptyList(); // rktodo
    }

    /**
     * @return XML files representing individual data sets (XML files under named following pattern P00000.xml)
     */
    public Stream<Path> findDataSetFiles(final Path publishingPackagesDir) {
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


    private void assertRequiredArgs(final Path hippoImportDir, final Path nesstarUnzippedArchiveLocation) {

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
