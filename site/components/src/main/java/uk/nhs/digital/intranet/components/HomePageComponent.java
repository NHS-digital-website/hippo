package uk.nhs.digital.intranet.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.EssentialsContentComponent;
import org.springframework.web.util.UriComponentsBuilder;
import uk.nhs.digital.intranet.provider.AuthorizationProvider;

public class HomePageComponent extends EssentialsContentComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        final String authorizationUrl = UriComponentsBuilder.fromUriString(AuthorizationProvider.base_url)
            .pathSegment("authorize")
            .queryParam("client_id", AuthorizationProvider.applicationId)
            .queryParam("response_type", "code")
            .queryParam("redirect_uri", AuthorizationProvider.redirectUri)
            .queryParam("response_mode", "query")
            .queryParam("scope", "openid offline_access user.readbasic.all")
            .queryParam("state", "to-be-replaced")
            .toUriString();
        request.setAttribute("authorizationUrl", authorizationUrl);
    }
}
