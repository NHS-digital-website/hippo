package uk.nhs.digital.ps.migrator.task.importables;

import uk.nhs.digital.ps.migrator.model.hippo.Folder;
import uk.nhs.digital.ps.migrator.model.hippo.HippoImportableItem;
import uk.nhs.digital.ps.migrator.model.hippo.Publication;
import uk.nhs.digital.ps.migrator.model.hippo.Series;
import uk.nhs.digital.ps.migrator.model.nesstar.Catalog;
import uk.nhs.digital.ps.migrator.model.nesstar.CatalogStructure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static uk.nhs.digital.ps.migrator.task.ImportableItemsFactory.*;

public class NhsOutcomesFrameworkImportables {

    static public List<HippoImportableItem> create(final CatalogStructure catalogStructure, final Folder ciRootFolder) {
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
        final Folder rootFolder = toFolder(rootCatalog, ciRootFolder);

        // B)
        final String currentQuarterFolderName = "current";
        final Folder quarterlyPublicationFolder = newFolder(currentQuarterFolderName, rootFolder);
        quarterlyPublicationFolder.setLocalizedName("Current");

        // C)
        final Publication quarterlyPublication = newPublication("content", currentQuarterFolderName, quarterlyPublicationFolder);

        // D)
        final List<HippoImportableItem> domainsWithDatasets = rootCatalog.getChildCatalogs()
            .stream()
            .filter(domainCatalog -> !"NHS Outcomes Framework (NHS OF) summary dashboard and useful links".equals(domainCatalog.getLabel()))
            .flatMap(domainCatalog -> {
                final Folder domainFolder = toFolder(domainCatalog, quarterlyPublicationFolder);

                return Stream.concat(
                    Stream.of(domainFolder),
                    // E)
                    getImportableDatasetsFromCatalog(domainCatalog, domainFolder)
                );
            }).collect(toList());

        // F)
        final String archiveFolderName = "archive";
        final Folder archiveFolder = newFolder(archiveFolderName, rootFolder);
        archiveFolder.setLocalizedName("Archive");

        // G
        final Series series = toSeries(archiveFolder, "Archive NHS Outcomes Framework Indicators");

        importableItems.add(rootFolder);
        importableItems.add(quarterlyPublicationFolder);
        importableItems.add(quarterlyPublication);
        importableItems.addAll(domainsWithDatasets);
        importableItems.add(archiveFolder);
        importableItems.add(series);

        return importableItems;
    }

    private static Stream<HippoImportableItem> getImportableDatasetsFromCatalog(Catalog rootCatalog, Folder rootFolder) {
        List<Catalog> catalogs = rootCatalog.getChildCatalogs();

        if (catalogs.isEmpty()) {
            return rootCatalog.findPublishingPackages().stream().map(domainPublishingPackage ->
                toDataSet(domainPublishingPackage, rootFolder)
            );
        }

        return catalogs.stream()
            .flatMap(domainCatalog -> getImportableDatasetsFromCatalog(domainCatalog, rootFolder));
    }
}
