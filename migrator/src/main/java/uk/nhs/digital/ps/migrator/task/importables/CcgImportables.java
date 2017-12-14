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

public class CcgImportables {

    private final ImportableItemsFactory importableItemsFactory;

    public CcgImportables(final ImportableItemsFactory importableItemsFactory) {
        this.importableItemsFactory = importableItemsFactory;
    }

    public List<HippoImportableItem> create(final CatalogStructure catalogStructure, final Folder ciRootFolder) {

        // Target CMS structure:
        //
        // A)  CCG Outcomes Indicator Set                           FOLDER
        // B)    Current                                            FOLDER
        // C)      content                                          PUBLICATION
        // D)        folders per node (domain x)                    FOLDER
        // E)          DataSet documents from subfolders            DATASET
        // F)    Archive                                            FOLDER
        // G)      content                                          SERIES
        // H)      2016                                             PUBLICATION (to be created manually by editors)

        // A)
        final Catalog ccgRootCatalog = catalogStructure.findCatalogByLabel("CCG Outcomes Indicator Set");
        final Folder ccgRootFolder = importableItemsFactory.toFolder(ciRootFolder, ccgRootCatalog);

        // B)
        final Folder currentPublicationFolder = importableItemsFactory.newFolder(ccgRootFolder, "Current");

        // C)
        final Publication currentPublication = importableItemsFactory.newPublication(
            currentPublicationFolder, "content", ccgRootFolder.getLocalizedName());

        // D)
        // There is only one level of Domains under CCG so it's enough to just iterate over them rather than walk a tree
        final List<Catalog> domainCatalogs = ccgRootCatalog.getChildCatalogs();

        final List<HippoImportableItem> domainsWithDatasets = domainCatalogs.stream()
            .flatMap(domainCatalog -> {
                final Folder domainFolder = importableItemsFactory.toFolder(currentPublicationFolder, domainCatalog);

                return Stream.concat(
                    Stream.of(domainFolder),
                    // E)
                    domainCatalog.findPublishingPackages().stream().map(domainPublishingPackage ->
                        importableItemsFactory.toDataSet(domainFolder, domainPublishingPackage)
                    )
                );

            }).collect(toList());

        // F)
        final Folder archiveFolder = importableItemsFactory.newFolder(ccgRootFolder, "Archive");

        // G
        final Series series = importableItemsFactory.newSeries(archiveFolder, "Archived " + ccgRootFolder.getLocalizedName());

        final List<HippoImportableItem> importableItems = new ArrayList<>();
        importableItems.add(ccgRootFolder);
        importableItems.add(currentPublicationFolder);
        importableItems.add(currentPublication);
        importableItems.addAll(domainsWithDatasets);
        importableItems.add(archiveFolder);
        importableItems.add(series);

        return importableItems;
    }
}
