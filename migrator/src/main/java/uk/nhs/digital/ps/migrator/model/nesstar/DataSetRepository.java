package uk.nhs.digital.ps.migrator.model.nesstar;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class DataSetRepository {

    private final Map<String, PublishingPackage> datasetsByIds;


    public DataSetRepository(final List<PublishingPackage> publishingPackages) {
        datasetsByIds = publishingPackages.stream()
            .collect(toMap(
                PublishingPackage::getUniqueIdentifier,
                dataSet -> dataSet)
            );
    }

    public PublishingPackage findById(final String id) {
        return datasetsByIds.get(id);
    }
}
