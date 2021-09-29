package uk.nhs.digital.crisp;

import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.core.resource.jackson.JacksonResource;
import org.onehippo.cms7.crisp.core.resource.jackson.SimpleJacksonRestTemplateResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Designed to be used with SVG resources. It will only cache the resource if it contains an SVG.
 */
public class SvgSimpleJacksonRestTemplateResourceResolver extends SimpleJacksonRestTemplateResourceResolver {

    private static final Logger log = LoggerFactory.getLogger(SvgSimpleJacksonRestTemplateResourceResolver.class);
    private static final Pattern svgPattern = Pattern.compile("<svg[^>]+?>", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    @Override
    public boolean isCacheable(final Resource resource) {
        if (this.isCacheEnabled() && resource instanceof JacksonResource) {
            String data = resource.getValue("data", String.class);
            if (Objects.nonNull(data)) {
                byte[] decodedBytes = Base64.getDecoder().decode(data);
                String decodedString = new String(decodedBytes);
                Matcher regexMatcher = svgPattern.matcher(decodedString);
                if (regexMatcher.find()) {
                    return true;
                } else {
                    log.warn("Could not cache SVG resource. Its body contained: " + decodedString);
                }
            }
        }
        return false;
    }

}
