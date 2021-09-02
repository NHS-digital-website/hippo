package uk.nhs.digital.common.components.apicatalogue;

import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UrlGeneratorDirective implements TemplateDirectiveModel {

    @Override public void execute(final Environment environment,
                                  final Map parameters,
                                  final TemplateModel[] loopVars,  // ignored
                                  final TemplateDirectiveBody body // ignored
    ) throws TemplateException, IOException {

        final String baseUrl = ((SimpleScalar) parameters.get(Param.baseUrl.name())).getAsString();
        final boolean showDeprecatedAndRetired = ((TemplateBooleanModel) parameters.get(Param.showDeprecatedAndRetired.name())).getAsBoolean();
        final TemplateSequenceModel filtersModelWrapper = ((SimpleSequence) parameters.get(Param.filters.name()));

        final List<String> filters = new ArrayList<>(filtersModelWrapper.size());
        for (int i = 0; i < filtersModelWrapper.size(); i++) {
            filters.add(((SimpleScalar)filtersModelWrapper.get(i)).getAsString());
        }

        final List<String> params = new ArrayList<>();

        if (showDeprecatedAndRetired) {
            params.add(Param.showDeprecatedAndRetired.name());
        }

        if (!filters.isEmpty()) {
            params.add(Param.filters.name() + "=" + String.join(",", filters));
        }

        final StringBuilder url = new StringBuilder(baseUrl);

        if (!params.isEmpty()) {
            url
                .append("?")
                .append(String.join("&", params));
        }

        environment.getOut().append(url.toString());
    }

    enum Param {
        baseUrl,
        showDeprecatedAndRetired,
        filters
    }
}
