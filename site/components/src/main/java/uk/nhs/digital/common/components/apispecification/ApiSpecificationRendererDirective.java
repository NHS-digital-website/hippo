package uk.nhs.digital.common.components.apispecification;

import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.site.HstServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class ApiSpecificationRendererDirective implements TemplateDirectiveModel {

    private static final Logger log = LoggerFactory.getLogger(ApiSpecificationRendererDirective.class);

    @Override public void execute(final Environment environment,
                                  final Map parameters,
                                  final TemplateModel[] loopVars,  // ignored
                                  final TemplateDirectiveBody body // ignored
    ) throws TemplateException, IOException {

        final OpenApiSpecificationJsonToHtmlConverter converter
            = HstServices.getComponentManager().getComponent("apiSpecificationRenderer");

        final String specificationJson = stringParam(parameters, "specificationJson").filter(StringUtils::isNotBlank).orElse(null);

        final String path = stringParam(parameters, "path").orElse("not available");

        if (specificationJson == null) {
            log.debug("No API specification JSON available for {}", path);
            return;
        }

        try {
            log.debug("Rendering API specification {}: start", path);
            final String specificationHtml = converter.htmlFrom(specificationJson);
            log.debug("Rendering API specification {}: done", path);

            environment.getOut().append(specificationHtml);

        } catch (final RuntimeException | IOException e) {
            log.error("Rendering API specification " + path + ": failed", e);
            throw e;
        }
    }

    private Optional<String> stringParam(final Map parameters, final String parameterName) {
        return Optional.ofNullable(parameters.get(parameterName))
            .map(SimpleScalar.class::cast)
            .map(SimpleScalar::getAsString);
    }
}
