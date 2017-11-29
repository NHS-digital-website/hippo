package uk.nhs.digital.ps.migrator.task.importables;

import uk.nhs.digital.ps.migrator.model.hippo.Folder;
import uk.nhs.digital.ps.migrator.model.hippo.HippoImportableItem;
import uk.nhs.digital.ps.migrator.model.hippo.Publication;
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
        // B)  Outcomes Framework publication 'content' document    PUBLICATION
        // C)    folders per node                                   FOLDER
        // D)      DataSet documents from subfolders                DATASET

        // A)
        final Catalog rootCatalog = catalogStructure.findCatalogByLabel("NHS Outcomes Framework");
        final Folder rootFolder = toFolder(rootCatalog, ciRootFolder);

        // B)
        final Publication publication = toPublication(rootCatalog, rootFolder);

        // C)
        final List<HippoImportableItem> domainsWithDatasets = rootCatalog.getChildCatalogs()
            .stream()
            .filter(domainCatalog -> !"NHS Outcomes Framework (NHS OF) summary dashboard and useful links".equals(domainCatalog.getLabel()))
            .flatMap(domainCatalog -> {
                final Folder domainFolder = toFolder(domainCatalog, rootFolder);

                return Stream.concat(
                    Stream.of(domainFolder),
                    // D)
                    getImportableDatasetsFromCatalog(domainCatalog, domainFolder)
                );
            }).collect(toList());

        importableItems.add(rootFolder);
        importableItems.add(publication);
        importableItems.addAll(domainsWithDatasets);

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
