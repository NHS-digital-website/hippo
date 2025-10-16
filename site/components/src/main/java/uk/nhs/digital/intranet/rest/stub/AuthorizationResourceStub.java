package uk.nhs.digital.intranet.rest.stub;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
@Path("/intranet/stub/")
public class AuthorizationResourceStub {

    private static final String AUTHORIZATION_CODE = "auth_code";

    @GET
    @Path("/authorize")
    public boolean processResponse(
        @Context HttpServletResponse response,
        @QueryParam("redirect_uri") final String redirectUri) throws IOException {

        final String uri = UriComponentsBuilder
            .fromUriString(redirectUri)
            .queryParam("code", AUTHORIZATION_CODE)
            .toUriString();
        response.sendRedirect(uri);
        return true;
    }
}
