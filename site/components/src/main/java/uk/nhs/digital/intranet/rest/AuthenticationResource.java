package uk.nhs.digital.intranet.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.intranet.model.AccessToken;
import uk.nhs.digital.intranet.model.exception.AuthorizationException;
import uk.nhs.digital.intranet.provider.AuthorizationProvider;
import uk.nhs.digital.intranet.provider.CookieProvider;

import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
@Path("/intranet/auth")
public class AuthenticationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationResource.class);

    private final AuthorizationProvider authorizationProvider;
    private final CookieProvider cookieProvider;
    private final String redirectUri;

    public AuthenticationResource(final AuthorizationProvider authorizationProvider, final CookieProvider cookieProvider, final String redirectUri) {
        this.authorizationProvider = authorizationProvider;
        this.cookieProvider = cookieProvider;
        this.redirectUri = redirectUri;
    }

    @GET
    @Path("/response")
    public boolean processResponse(
        @Context HttpServletResponse response,
        @QueryParam("code") final String authorizationCode) throws IOException {

        try {
            final AccessToken accessToken = authorizationProvider.processAuthorizationResponse(authorizationCode);
            final Cookie accessTokenCookie = cookieProvider.getAccessTokenCookie(accessToken);
            final Cookie refreshTokenCookie = cookieProvider.getRefreshTokenCookie(accessToken);
            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);
        } catch (final AuthorizationException e) {
            LOGGER.error("Received exception with status code {} from Microsoft Graph API when trying to acquire access token.", e.getStatusCode().value(), e.getCause());
        } finally {
            response.sendRedirect(redirectUri);
        }
        return true;
    }
}
