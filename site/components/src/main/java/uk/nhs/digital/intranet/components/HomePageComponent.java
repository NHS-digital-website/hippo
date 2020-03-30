package uk.nhs.digital.intranet.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.EssentialsContentComponent;
import org.springframework.web.util.UriComponentsBuilder;
import uk.nhs.digital.intranet.provider.AuthorizationProvider;

public class HomePageComponent extends EssentialsContentComponent {

    private final String applicationId;
    private final String redirectUri;

    public HomePageComponent(String applicationId, String redirectUri) {
        this.applicationId = applicationId;
        this.redirectUri = redirectUri;
    }

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        final String authorizationUrl = UriComponentsBuilder.fromUriString(AuthorizationProvider.base_url)
            .pathSegment("authorize")
            .queryParam("client_id", applicationId)
            .queryParam("response_type", "code")
            .queryParam("redirect_uri", redirectUri)
            .queryParam("response_mode", "query")
            .queryParam("scope", "openid offline_access user.readbasic.all")
            .queryParam("state", "to-be-replaced")
            .toUriString();
        request.setAttribute("authorizationUrl", authorizationUrl);
    }
}
