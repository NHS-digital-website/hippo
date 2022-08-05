package uk.nhs.digital.website.rest;

import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.onehippo.cms7.essentials.components.rest.BaseRestResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.website.beans.ApiSpecification;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
@Path("/oas/")
public class ApiSpecificationResource extends BaseRestResource {

    private static final Logger log = LoggerFactory.getLogger(ApiSpecificationResource.class);

    @GET
    @Path("/{specId}/")
    public String getRawOasSpecData(
        @Context HttpServletRequest servletRequest,
        @Context HttpServletResponse servletResponse,
        @Context UriInfo uriInfo,
        @PathParam("specId") String specId) {
        try {
            final HstQueryResult result = getHstQueryResult(specId);

            HippoBeanIterator iterator = result.getHippoBeans();
            while (iterator.hasNext()) {
                ApiSpecification apiSpecification = (ApiSpecification) iterator.nextHippoBean();

                if (apiSpecification != null && apiSpecification.getSpecificationId().matches(specId)) {
                    return apiSpecification.getJson();
                }
            }
            throw new NotFoundException(
                String.format("No spec found with specification id \"%s\"", specId)
            );
        } catch (Exception e) {
            log.error(
                String.format("Error finding spec with specification id \"%s\": %s", specId, e)
            );

            throw new NotFoundException();
        }
    }

    private HstQueryResult getHstQueryResult(String specId) throws QueryException {
        HstQuery query = HstQueryBuilder.create(RequestContextProvider.get().getSiteContentBaseBean())
            .where(constraint("website:specification_id").equalToCaseInsensitive(specId))
            .ofTypes("website:apispecification")
            .build();
        query.setLimit(1);
        HstQueryResult result = query.execute();
        return result;
    }
}
