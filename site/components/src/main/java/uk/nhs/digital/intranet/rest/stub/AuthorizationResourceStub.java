package uk.nhs.digital.intranet.rest.stub;

import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

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
