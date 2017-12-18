package uk.nhs.digital.ps.migrator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.nhs.digital.ps.migrator.task.*;

import java.util.List;

import static java.util.Arrays.asList;

@Configuration
public class MigratorConfiguration {

    @Bean
    public List<Task> tasks(final ExecutionParameters executionParameters) {
        return asList(
            new UnzipNesstarFileTask(executionParameters),
            new GenerateImportContentTask(executionParameters),
            new GenerateTaxonomyTask(executionParameters)
        );
    }

    @Bean
    public ExecutionParameters sharedTaskParameters() {
        return new ExecutionParameters();
    }

    @Bean
    public ExecutionConfigurer commandLineArgsParser(final ExecutionParameters executionParameters) {
        return new ExecutionConfigurer(executionParameters);
    }

}
