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

public class SocialCareImportables {

    private final ImportableItemsFactory importableItemsFactory;

    public SocialCareImportables(final ImportableItemsFactory importableItemsFactory) {
        this.importableItemsFactory = importableItemsFactory;
    }

    public List<HippoImportableItem> create(final CatalogStructure catalogStructure, final Folder ciRootFolder) {

        // Target CMS structure:
        //
        // A)  Adult Social Care Outcomes Framework (ASCOF)         FOLDER
        // B)    Current                                            FOLDER
        // C)      content                                          PUBLICATION
        // D)        folders per node (domain x)                    FOLDER
        // E)          DataSet documents from subfolders            DATASET
        // F)    Archive                                            FOLDER
        // G)      content                                          SERIES
        // H)      2016                                             PUBLICATION (to be created manually by authors)

        // A)
        final Catalog rootCatalog = catalogStructure.findCatalogByLabel("Adult Social Care Outcomes Framework (ASCOF)");
        final Folder rootFolder = importableItemsFactory.toFolder(ciRootFolder, rootCatalog);

        // B)
        final Folder currentPublicationFolder = importableItemsFactory.newFolder(rootFolder, "Current");

        // C)
        final Publication currentPublication = importableItemsFactory.newPublication(
            currentPublicationFolder, "content", rootFolder.getLocalizedName());

        // D)
        final List<Catalog> domainCatalogs = rootCatalog.getChildCatalogs();

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
        final Folder archiveFolder = importableItemsFactory.newFolder(rootFolder, "Archive");

        // G
        final Series series = importableItemsFactory.newSeries(archiveFolder, "Archived " + rootFolder.getLocalizedName());

        final List<HippoImportableItem> importableItems = new ArrayList<>();
        importableItems.add(rootFolder);
        importableItems.add(currentPublicationFolder);
        importableItems.add(currentPublication);
        importableItems.addAll(domainsWithDatasets);
        importableItems.add(archiveFolder);
        importableItems.add(series);

        return importableItems;
    }

}
