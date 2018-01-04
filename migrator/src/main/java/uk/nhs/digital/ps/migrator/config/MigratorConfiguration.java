package uk.nhs.digital.ps.migrator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.nhs.digital.ps.migrator.MigrationReport;
import uk.nhs.digital.ps.migrator.task.*;
import uk.nhs.digital.ps.migrator.task.importables.CcgImportables;
import uk.nhs.digital.ps.migrator.task.importables.CompendiumImportables;
import uk.nhs.digital.ps.migrator.task.importables.NhsOutcomesFrameworkImportables;
import uk.nhs.digital.ps.migrator.task.importables.SocialCareImportables;

import java.util.List;

import static java.util.Arrays.asList;

@Configuration
public class MigratorConfiguration {

    @Bean
    public List<MigrationTask> tasks(final ExecutionParameters executionParameters,
                                     final ImportableItemsFactory importableItemsFactory,
                                     final SocialCareImportables socialCareImportables,
                                     final CcgImportables ccgImportables,
                                     final NhsOutcomesFrameworkImportables nhsOutcomesFrameworkImportables,
                                     final CompendiumImportables compendiumImportables,
                                     final ImportableFileWriter importableFileWriter,
                                     final MigrationReport migrationReport) {

        return asList(
            new UnzipNesstarExportFileTask(executionParameters),
            new GenerateNesstarImportContentTask(
                executionParameters,
                importableItemsFactory,
                socialCareImportables,
                ccgImportables,
                nhsOutcomesFrameworkImportables,
                compendiumImportables,
                importableFileWriter,
                migrationReport),
            new GenerateTaxonomyTask(executionParameters)
        );
    }

    @Bean
    public ExecutionParameters sharedTaskParameters() {
        return new ExecutionParameters();
    }

    @Bean
    public MigrationReport migrationReport(final ExecutionParameters executionParameters) {
        return new MigrationReport(executionParameters);
    }

    @Bean
    public ExecutionConfigurator commandLineArgsParser(final ExecutionParameters executionParameters) {
        return new ExecutionConfigurator(executionParameters);
    }

    @Bean
    public ImportableItemsFactory importableItemsFactory(final ExecutionParameters executionParameters,
                                                         final MigrationReport migrationReport)
    {
        return new ImportableItemsFactory(executionParameters, migrationReport);
    }

    @Bean
    public ImportableFileWriter importableFileWriter(final MigrationReport migrationReport) {
        return new ImportableFileWriter(migrationReport);
    }

    @Bean
    public CcgImportables ccgImportables(final ImportableItemsFactory importableItemsFactory) {
        return new CcgImportables(importableItemsFactory);
    }

    @Bean
    public SocialCareImportables socialCareImportables(final ImportableItemsFactory importableItemsFactory) {
        return new SocialCareImportables(importableItemsFactory);
    }

    @Bean
    public NhsOutcomesFrameworkImportables nhsOutcomesFrameworkImportables(final ImportableItemsFactory importableItemsFactory) {
        return new NhsOutcomesFrameworkImportables(importableItemsFactory);
    }

    @Bean
    public CompendiumImportables compendiumImportables(final ExecutionParameters executionParameters,
                                                       final ImportableItemsFactory importableItemsFactory,
                                                       final MigrationReport migrationReport
    ) {
        return new CompendiumImportables(executionParameters, importableItemsFactory, migrationReport);
    }
}
