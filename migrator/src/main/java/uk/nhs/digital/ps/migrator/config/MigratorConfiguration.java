package uk.nhs.digital.ps.migrator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.nhs.digital.ps.migrator.model.hippo.TaxonomyMigrator;
import uk.nhs.digital.ps.migrator.report.MigrationReport;
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
                                     final NesstarImportableItemsFactory nesstarImportableItemsFactory,
                                     final SocialCareImportables socialCareImportables,
                                     final CcgImportables ccgImportables,
                                     final NhsOutcomesFrameworkImportables nhsOutcomesFrameworkImportables,
                                     final CompendiumImportables compendiumImportables,
                                     final ImportableFileWriter importableFileWriter,
                                     final MigrationReport migrationReport,
                                     final TaxonomyMigrator taxonomyMigrator) {

        return asList(
            new UnzipNesstarExportFileTask(executionParameters),
            new GenerateNesstarImportContentTask(
                executionParameters,
                nesstarImportableItemsFactory,
                socialCareImportables,
                ccgImportables,
                nhsOutcomesFrameworkImportables,
                compendiumImportables,
                importableFileWriter,
                migrationReport,
                taxonomyMigrator),
            new GenerateTaxonomyTask(executionParameters,
                taxonomyMigrator)
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
    public NesstarImportableItemsFactory importableItemsFactory(final ExecutionParameters executionParameters,
                                                                final MigrationReport migrationReport,
                                                                final TaxonomyMigrator taxonomyMigrator)
    {
        return new NesstarImportableItemsFactory(executionParameters, migrationReport, taxonomyMigrator);
    }

    @Bean
    public ImportableFileWriter importableFileWriter(final MigrationReport migrationReport) {
        return new ImportableFileWriter(migrationReport);
    }

    @Bean
    public CcgImportables ccgImportables(final NesstarImportableItemsFactory nesstarImportableItemsFactory) {
        return new CcgImportables(nesstarImportableItemsFactory);
    }

    @Bean
    public SocialCareImportables socialCareImportables(final NesstarImportableItemsFactory nesstarImportableItemsFactory) {
        return new SocialCareImportables(nesstarImportableItemsFactory);
    }

    @Bean
    public NhsOutcomesFrameworkImportables nhsOutcomesFrameworkImportables(final NesstarImportableItemsFactory nesstarImportableItemsFactory) {
        return new NhsOutcomesFrameworkImportables(nesstarImportableItemsFactory);
    }

    @Bean
    public CompendiumImportables compendiumImportables(final ExecutionParameters executionParameters,
                                                       final NesstarImportableItemsFactory nesstarImportableItemsFactory,
                                                       final MigrationReport migrationReport
    ) {
        return new CompendiumImportables(executionParameters, nesstarImportableItemsFactory, migrationReport);
    }

    @Bean
    public TaxonomyMigrator taxonomyMigrator(final MigrationReport migrationReport,
                                             final ExecutionParameters executionParameters) {
        return new TaxonomyMigrator(migrationReport, executionParameters);
    }
}
