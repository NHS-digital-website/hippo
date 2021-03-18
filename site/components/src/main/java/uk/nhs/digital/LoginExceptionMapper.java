package uk.nhs.digital;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.website.rest.PublicationResponse;

import javax.jcr.LoginException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * This extension provider can be configured to be invoked whenever
 * MyCustomSecurityException occurs. In that case, this provider simply
 * returns a forbidden access status code. This provider can make the
 * remaining JAX-RS Resource operation implementations much more simplified.
 * Refer to the JAX-RS Specification for more detail.
 */
@Provider
public class LoginExceptionMapper
        implements ExceptionMapper<LoginException> {
    private static final Logger log = LoggerFactory.getLogger(LoginExceptionMapper.class);

    public Response toResponse(LoginException ex) {
        String message = "User is not authorised ";
        log.error(message, ex);
        return Response.status(Response.Status.UNAUTHORIZED.getStatusCode()).entity(new PublicationResponse(message)).build();
    }
}
