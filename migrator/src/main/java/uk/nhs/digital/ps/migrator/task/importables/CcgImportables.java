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

public class CcgImportables {

    static public List<HippoImportableItem> create(final CatalogStructure catalogStructure, final Folder ciRootFolder) {

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
}
