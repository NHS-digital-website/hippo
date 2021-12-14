package uk.nhs.digital.common.components.apicatalogue;

import freemarker.core.Environment;
import freemarker.template.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class LogRenderingDirective implements TemplateDirectiveModel { // rktodo remove

    private static final Logger log = LoggerFactory.getLogger(LogRenderingDirective.class);

    @Override public void execute(final Environment environment,
                                  final Map parameters,
                                  final TemplateModel[] loopVars,  // ignored
                                  final TemplateDirectiveBody body // ignored
    ) throws TemplateException, IOException {

        final String name = Optional.ofNullable((SimpleScalar) parameters.get("name")).map(SimpleScalar::getAsString).orElse("");
        final long durationInSecs = Optional.ofNullable((SimpleNumber) parameters.get("durationInSecs"))
            .map(simpleNumber -> simpleNumber.getAsNumber().longValue()).orElse(0L);

        try {
            log.debug("SIMULATING LONG RENDERING of '{}' for duration of {} seconds: start.", name, durationInSecs);

            Thread.sleep(1000 * durationInSecs);

            log.debug("SIMULATING LONG RENDERING of '{}' for duration of {} seconds: done.", name, durationInSecs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
