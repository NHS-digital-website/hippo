package uk.nhs.digital.ps.migrator.task.importables;

import uk.nhs.digital.ps.migrator.model.hippo.Folder;
import uk.nhs.digital.ps.migrator.model.hippo.HippoImportableItem;
import uk.nhs.digital.ps.migrator.model.hippo.Publication;
import uk.nhs.digital.ps.migrator.model.hippo.Series;
import uk.nhs.digital.ps.migrator.model.nesstar.Catalog;
import uk.nhs.digital.ps.migrator.model.nesstar.CatalogStructure;
import uk.nhs.digital.ps.migrator.task.ImportableItemsFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class NhsOutcomesFrameworkImportables {

    private final ImportableItemsFactory importableItemsFactory;

    public NhsOutcomesFrameworkImportables(final ImportableItemsFactory importableItemsFactory) {
        this.importableItemsFactory = importableItemsFactory;
    }

    public List<HippoImportableItem> create(final CatalogStructure catalogStructure, final Folder ciRootFolder) {
        final List<HippoImportableItem> importableItems = new ArrayList<>();

        // Target CMS structure:
        //
        // A)  NHS Outcomes Framework                               FOLDER
        // B)    Current                                            FOLDER
        // C)      content                                          PUBLICATION
        // D)        folders per node                               FOLDER
        // E)          DataSet documents from subfolders            DATASET
        // F)    Archive                                            FOLDER
        // G)      content                                          SERIES
        // H)      2016                                             PUBLICATION (to be created manually by editors)

        // A)
        final Catalog rootCatalog = catalogStructure.findCatalogByLabel("NHS Outcomes Framework");
        final Folder nfoRootFolder = importableItemsFactory.toFolder(ciRootFolder, rootCatalog);

        // B)
        final Folder currentPublicationFolder = importableItemsFactory.newFolder(nfoRootFolder, "Current");

        // C)
        final Publication currentPublication = importableItemsFactory.newPublication(
            currentPublicationFolder, "content", nfoRootFolder.getLocalizedName()
        );

        // D)
        final List<HippoImportableItem> domainsWithDatasets = rootCatalog.getChildCatalogs()
            .stream()
            .filter(domainCatalog -> !"NHS Outcomes Framework (NHS OF) summary dashboard and useful links".equals(domainCatalog.getLabel()))
            .flatMap(domainCatalog -> {
                final Folder domainFolder = importableItemsFactory.toFolder(currentPublicationFolder, domainCatalog);

                return Stream.concat(
                    Stream.of(domainFolder),
                    // E)
                    getImportableDatasetsFromCatalog(domainCatalog, domainFolder)
                );
            }).collect(toList());

        // F)
        final Folder archiveFolder = importableItemsFactory.newFolder(nfoRootFolder, "Archive");

        // G
        final Series series = importableItemsFactory.newSeries(archiveFolder, "Archived " + nfoRootFolder.getLocalizedName());

        importableItems.add(nfoRootFolder);
        importableItems.add(currentPublicationFolder);
        importableItems.add(currentPublication);
        importableItems.addAll(domainsWithDatasets);
        importableItems.add(archiveFolder);
        importableItems.add(series);

        return importableItems;
    }

    private Stream<HippoImportableItem> getImportableDatasetsFromCatalog(Catalog rootCatalog, Folder rootFolder) {
        List<Catalog> catalogs = rootCatalog.getChildCatalogs();

        if (catalogs.isEmpty()) {
            return rootCatalog.findPublishingPackages().stream().map(domainPublishingPackage ->
                importableItemsFactory.toDataSet(rootFolder, domainPublishingPackage)
            );
        }

        return catalogs.stream()
            .flatMap(domainCatalog -> getImportableDatasetsFromCatalog(domainCatalog, rootFolder));
    }
}
