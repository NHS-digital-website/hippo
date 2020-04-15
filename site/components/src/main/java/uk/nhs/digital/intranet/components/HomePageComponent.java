package uk.nhs.digital.intranet.components;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.onehippo.cms7.essentials.components.EssentialsContentComponent;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import uk.nhs.digital.intranet.utils.Constants;

public class HomePageComponent extends EssentialsContentComponent {

    private final String applicationId;
    private final String redirectUri;
    private final String baseUri;

    public HomePageComponent(final String applicationId, final String redirectUri, final String baseUri) {
        this.applicationId = applicationId;
        this.redirectUri = redirectUri;
        this.baseUri = baseUri;
    }

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        final HstRequestContext requestContext = RequestContextProvider.get();
        final String authorizationUrl = UriComponentsBuilder.fromUriString(baseUri)
            .pathSegment("authorize")
            .queryParam("client_id", applicationId)
            .queryParam("response_type", "code")
            .queryParam("redirect_uri", redirectUri)
            .queryParam("response_mode", "query")
            .queryParam("scope", "openid offline_access user.read.all")
            .queryParam("state", "to-be-replaced")
            .toUriString();
        request.setAttribute("authorizationUrl", authorizationUrl);
        request.setAttribute("loginRequired", !hasValidAccessToken(requestContext));
    }

    private boolean hasValidAccessToken(HstRequestContext requestContext) {
        return StringUtils.hasText((String) requestContext.getAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME));
    }
}
