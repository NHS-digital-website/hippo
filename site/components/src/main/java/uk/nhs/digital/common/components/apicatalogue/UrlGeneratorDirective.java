package uk.nhs.digital.common.components.apicatalogue;

import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

public class UrlGeneratorDirective implements TemplateDirectiveModel {

    @Override public void execute(final Environment environment,
                                  final Map parameters,
                                  final TemplateModel[] loopVars,  // ignored
                                  final TemplateDirectiveBody body // ignored
    ) throws TemplateException, IOException {

        final String baseUrl = ((SimpleScalar) parameters.get(Param.baseUrl.name())).getAsString();
        final boolean showRetired = ((TemplateBooleanModel) parameters.get(Param.showRetired.name())).getAsBoolean();
        final TemplateSequenceModel filtersModelWrapper = (SimpleSequence) parameters.get(Param.filters.name());

        final Map<String, List<String>> params = new LinkedHashMap<>();

        prepareshowRetiredParam(params, showRetired);

        prepareFilterParams(params, filtersModelWrapper);

        final String url = buildUrl(baseUrl, params);

        environment.getOut().append(url);
    }

    private void prepareshowRetiredParam(final Map<String, List<String>> params,
                                                      final boolean showRetired
    ) {
        if (showRetired) {
            params.put(Param.showRetired.name(), Collections.emptyList());
        }
    }

    private void prepareFilterParams(final Map<String, List<String>> params,
                                     final TemplateSequenceModel filtersModelWrapper
    ) throws TemplateModelException {

        final List<String> filters = new ArrayList<>(filtersModelWrapper.size());
        for (int i = 0; i < filtersModelWrapper.size(); i++) {
            filters.add(((SimpleScalar)filtersModelWrapper.get(i)).getAsString());
        }

        if (!filters.isEmpty()) {
            final List<String> urlEncodedFilters = filters.stream().map(this::urlEncode).collect(Collectors.toList());

            params.put(Param.filter.name(), urlEncodedFilters);
        }
    }

    private String buildUrl(final String baseUrl, final Map<String, List<String>> params) {

        final StringBuilder url = new StringBuilder(baseUrl);

        if (!params.isEmpty()) {
            url.append("?");

            params.forEach((name, values) -> {
                if (values.isEmpty()) {

                    addParamDelimiterIfNotFirstParam(url);

                    url.append(name);

                } else {
                    values.forEach(value -> {

                        addParamDelimiterIfNotFirstParam(url);

                        url.append(name).append("=").append(value);
                    });
                }
            });
        }

        return url.toString();
    }

    private void addParamDelimiterIfNotFirstParam(final StringBuilder url) {
        if ('?' != url.charAt(url.length() - 1)) {
            url.append("&");
        }
    }

    private String urlEncode(final String filterKey) {
        try {
            return URLEncoder.encode(filterKey, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to URL-encode filter key '" + filterKey + "'", e);
        }
    }

    enum Param {
        baseUrl,
        showRetired,
        filter,
        filters
    }
}
