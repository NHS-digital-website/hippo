package uk.nhs.digital.common.components.apispecification;

import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.site.HstServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.cache.Cache;

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

        final String specificationJson = stringParam(parameters, "specificationJson").filter(StringUtils::isNotBlank).orElse(null);
        final String documentHandleUuid = stringParam(parameters, "documentHandleUuid").filter(StringUtils::isNotBlank).orElse(null);
        final String path = stringParam(parameters, "path").orElse("not available");

        final OpenApiSpecificationJsonToHtmlConverter converter = getComponent("apiSpecificationRenderer");
        final Cache<String, String> cache = getComponent("heavyContentCache");

        if (specificationJson == null) {
            log.warn("No API specification JSON available for {} ({}).", path, documentHandleUuid);
            return;
        }

        try {
            final String specificationHtml = cache.get(
                documentHandleUuid,
                () -> renderSpecHtml(converter, specificationJson, path, documentHandleUuid)
            );

            environment.getOut().append(specificationHtml);

        } catch (final RuntimeException | IOException e) {
            log.error("Rendering API specification: FAILED; " + path + "(" + documentHandleUuid + ").", e);
            throw e;
        }
    }

    private <T> T getComponent(final String componentName) {
        return HstServices.getComponentManager().getComponent(componentName);
    }

    private String renderSpecHtml(
        final OpenApiSpecificationJsonToHtmlConverter converter,
        final String specificationJson,
        final String path,
        final String documentHandleUuid
    ) {
        log.info("Rendering API specification: START; {} ({}).", path, documentHandleUuid);
        final String specificationHtml = converter.htmlFrom(specificationJson);
        log.info("Rendering API specification: DONE; {} ({}).", path, documentHandleUuid);

        return specificationHtml;
    }

    private Optional<String> stringParam(final Map parameters, final String parameterName) {
        return Optional.ofNullable(parameters.get(parameterName))
            .map(SimpleScalar.class::cast)
            .map(SimpleScalar::getAsString);
    }
}
