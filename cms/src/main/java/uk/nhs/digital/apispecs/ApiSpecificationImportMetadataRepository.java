package uk.nhs.digital.apispecs;

public interface ApiSpecificationImportMetadataRepository {

    ApiSpecificationImportMetadata findApiSpecificationImportMetadata();

    void save(ApiSpecificationImportMetadata apiSpecificationImportMetadata);
}
