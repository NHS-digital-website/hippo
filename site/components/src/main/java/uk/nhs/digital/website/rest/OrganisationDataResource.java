package uk.nhs.digital.website.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.onehippo.cms7.essentials.components.rest.BaseRestResource;
import org.onehippo.cms7.essentials.components.rest.ctx.DefaultRestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.model.OdsOrganisation;

import java.util.List;
import java.util.stream.Collectors;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
@Path("/orgname/")
public class OrganisationDataResource extends BaseRestResource {
    private static final Logger log = LoggerFactory.getLogger(CyberAlertResource.class);
    private List<OdsOrganisation> odsResults = null;

    @GET
    @Path("/")
    public List<OdsOrganisation> getOrg(@Context HttpServletRequest request, @Context HttpServletResponse servletResponse, @QueryParam("orgName") String orgName) {

        if (odsResults == null) {
            try {
                log.debug("Loading ODS Data");
                HstRequestContext context = new DefaultRestContext(this, request).getRequestContext();
                QueryManager manager = context.getSession().getWorkspace().getQueryManager();
                Query jcrQuery = manager.createQuery("/jcr:root/content/assets//*[@jcr:primaryType='externalstorage:resource']", "xpath");
                QueryResult execute = jcrQuery.execute();
                NodeIterator iterator = execute.getNodes();
                while (iterator.hasNext()) {
                    Node node = iterator.nextNode();
                    if (node.getPath().contains("/content/assets/ODS_Data")) {
                        Value val = node.getProperty("jcr:data").getValue();
                        // convert JSON array to Java List
                        odsResults = new ObjectMapper().readValue(val.getString().replace("\n", ""), new TypeReference<List<OdsOrganisation>>() {
                        });
                        break;
                    }
                }

            } catch (RepositoryException | JsonProcessingException e) {
                log.debug("Failed to load ODS Data ", e);
            }
        }
        List<OdsOrganisation> filterOrg =
            odsResults.stream()
                .filter(b -> (b.getOrgName() + " " + b.getCode()).toUpperCase().contains(orgName.toUpperCase()))
                .collect(Collectors.toList());
        return filterOrg;
    }
}
