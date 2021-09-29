package uk.nhs.digital.crisp;

import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.core.resource.jackson.JacksonResource;
import org.onehippo.cms7.crisp.core.resource.jackson.SimpleJacksonRestTemplateResourceResolver;

import java.util.Base64;
import java.util.Objects;

/**
 * Designed to be used with SVG resources. It will only cache the resource if it contains an SVG.
 */
public class SvgSimpleJacksonRestTemplateResourceResolver extends SimpleJacksonRestTemplateResourceResolver {

    @Override
    public boolean isCacheable(Resource resource) {
        if (this.isCacheEnabled() && resource instanceof JacksonResource) {
            String data = resource.getValue("data", String.class);
            if (Objects.nonNull(data)) {
                byte[] decodedBytes = Base64.getDecoder().decode(data);
                String decodedString = new String(decodedBytes);
                return decodedString.matches("(?i)<svg[^>]+?>");
            }
        }
        return false;
    }

}
