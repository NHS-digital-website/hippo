package uk.nhs.digital.ps.migrator.task;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.nhs.digital.ps.migrator.config.ExecutionParameters;
import uk.nhs.digital.ps.migrator.model.hippo.TaxonomyMigrator;
import uk.nhs.digital.ps.migrator.model.taxonomy.TaxonomyTerm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GenerateTaxonomyTask implements MigrationTask {

    private final ExecutionParameters executionParameters;
    private final TaxonomyMigrator taxonomyMigrator;

    public GenerateTaxonomyTask(final ExecutionParameters executionParameters, TaxonomyMigrator taxonomyMigrator) {
        this.executionParameters = executionParameters;
        this.taxonomyMigrator = taxonomyMigrator;
    }

    @Override
    public boolean isRequested() {
        return executionParameters.getTaxonomyDefinitionImportPath() != null;
    }

    @Override
    public void execute() {

        Path taxonomyDefinitionImportPath = executionParameters.getTaxonomyDefinitionImportPath();
        Path taxonomyDefinitionOutputPath = executionParameters.getTaxonomyDefinitionOutputPath();
        assertRequiredArgs(taxonomyDefinitionImportPath, taxonomyDefinitionOutputPath);

        taxonomyMigrator.init();
        TaxonomyTerm taxonomyDefinition = taxonomyMigrator.getTaxonomyDefinition();

        // Write out the JSON files
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            File dir = taxonomyDefinitionOutputPath.toFile();
            dir.mkdirs();
            // Prefix is so taxonomy gets imported first, before any documents
            mapper.writeValue(new File(dir, "000000_" + "publication_taxonomy.json"), taxonomyDefinition);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void assertRequiredArgs(final Path taxonomyDefinitionImportPath,
                                    final Path taxonomyDefinitionOutputPath) {

        if (taxonomyDefinitionOutputPath == null) {
            throw new IllegalArgumentException("Required Taxonomy Definition Output Path was not specified.");
        }

        if (taxonomyDefinitionImportPath == null) {
            throw new IllegalArgumentException("Required Taxonomy Definition Import Path was not specified.");
        }

        if (!Files.isRegularFile(taxonomyDefinitionImportPath)) {
            throw new IllegalArgumentException(
                "Taxonomy Definition Import Path does not exist: " + taxonomyDefinitionImportPath
            );
        }
    }
}
