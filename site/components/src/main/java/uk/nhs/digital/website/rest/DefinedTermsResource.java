package uk.nhs.digital.website.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.onehippo.cms7.essentials.components.rest.BaseRestResource;
import org.onehippo.cms7.essentials.components.rest.ctx.DefaultRestContext;
import uk.nhs.digital.website.beans.DefinedTerms;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
@Path("/definedterms/")
public class DefinedTermsResource extends BaseRestResource {

    @GET
    @Path("/")
    public Pageable<DefinedTerms> index(@Context HttpServletRequest request) {
        return findBeans(new DefaultRestContext(this, request), DefinedTerms.class);
    }
}
