package uk.nhs.digital.intranet.provider.stub;

import uk.nhs.digital.intranet.model.AccessToken;
import uk.nhs.digital.intranet.provider.AuthorizationProvider;

import java.util.UUID;

public class AuthorizationProviderStub implements AuthorizationProvider {

    private static final int TTL = 3600;

    @Override
    public AccessToken processAuthorizationResponse(final String authorizationCode) {
        final String token = UUID.randomUUID().toString();
        final String refreshToken = UUID.randomUUID().toString();
        return new AccessToken(token, refreshToken, TTL);
    }

    @Override
    public AccessToken refreshAccessToken(final AccessToken accessToken) {
        final String token = UUID.randomUUID().toString();
        final String refreshToken = UUID.randomUUID().toString();
        return new AccessToken(token, refreshToken, TTL);
    }
}
