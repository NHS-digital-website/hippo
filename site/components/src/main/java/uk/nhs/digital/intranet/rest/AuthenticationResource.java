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

    @GET
    @Path("/response")
    public boolean processResponse(
        @Context HttpServletResponse response,
        @QueryParam("code") final String authorizationCode) {

        try {
            final AccessToken accessToken = AuthorizationProvider.processAuthorizationResponse(authorizationCode);
            LOGGER.info("Received access token: {}", accessToken.getToken());
            final Cookie cookie = CookieProvider.getAccessTokenCookie(accessToken);
            response.addCookie(cookie);
            response.sendRedirect("http://localhost:8080/site/intranet");
        } catch (final AuthorizationException e) {
            LOGGER.error("Received exception with status code {} from Microsoft Graph API.", e.getStatusCode().value(), e.getCause());
        } catch (final IOException e) {
            LOGGER.error("Unexpected error encountered when redirecting to home page.", e);
        }
        return true;
    }
}
