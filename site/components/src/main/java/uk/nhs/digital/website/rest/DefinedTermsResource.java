package uk.nhs.digital.website.rest;

import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.onehippo.cms7.essentials.components.rest.BaseRestResource;
import org.onehippo.cms7.essentials.components.rest.ctx.DefaultRestContext;
import uk.nhs.digital.website.beans.DefinedTerms;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


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
