package uk.nhs.digital.intranet.provider;

import uk.nhs.digital.intranet.model.AccessToken;
import uk.nhs.digital.intranet.model.exception.AuthorizationException;

public interface AuthorizationProvider {

    AccessToken processAuthorizationResponse(final String authorizationCode) throws AuthorizationException;

    AccessToken refreshAccessToken(final AccessToken accessToken) throws AuthorizationException;
}
