package uk.nhs.digital.website.rest;

import static java.util.stream.Collectors.toList;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.apache.jackrabbit.util.Base64;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.annotations.Persistable;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.manager.workflow.WorkflowPersistenceManager;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.essentials.components.rest.BaseRestResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.beans.Publication;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import javax.jcr.*;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.validation.*;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class PublicationApiResource extends BaseRestResource {

    private static final Logger log = LoggerFactory.getLogger(PublicationApiResource.class);

    private static final String PUBLICATION_DOCTYPE = "publicationsystem:publication";

    private final Bucket bucket; // for rate limiting (throttling)
    private final Validator validator;

    public PublicationApiResource() {
        bucket = Bucket4j.builder()
                .addLimit(Bandwidth.classic(3, Refill.intervally(3, Duration.ofMinutes(1)))) //3 requests per minute for POC purposes.Not an actually good number.
                .build();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Example usage:
     * POST http://localhost:8080/site/restapi/publication
     * payload:
     * {
     * "path": "publication-system/pocfolder",
     * "title": "Test"
     * }
     */

    @Path("/publication")
    @POST
    @Persistable
    @Produces({MediaType.APPLICATION_JSON})
    public Response postPublication(@Valid PublicationModel model,
                                    @HeaderParam("authorization") String authorization) throws RepositoryException {
        // Authentication
        Session authenticatedSession = authenticateUser(authorization);

        // Validation
        Set<ConstraintViolation<PublicationModel>> violations = validator.validate(model);
        List<PublicationErrorItem> errorItems = violations.stream().map(PublicationErrorItem::new).collect(toList());
        if (!errorItems.isEmpty()) {
            authenticatedSession.logout();
            PublicationResponse response = new PublicationResponse("There were validations errors in some of the fields.");
            response.setValidationErrors(errorItems);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(response).build();
        }

        // Find series
        String pathToSeriesFolder = findPathToSeries(authenticatedSession, model.getSeries());
        if (pathToSeriesFolder == null || pathToSeriesFolder.trim().equalsIgnoreCase("")) {
            authenticatedSession.logout();
            String message = "Publication with title '" + model.getTitle() + "' could not be created because series titled: '" + model.getSeries() + "' was not found.";
            log.error(message);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).entity(new PublicationResponse(message)).build();
        }

        // Throttling check
        if (bucket.tryConsume(1)) {
            try {
                // Write publication
                final HstRequestContext rc = RequestContextProvider.get();
                writePublication(rc, authenticatedSession, pathToSeriesFolder, model);
                authenticatedSession.logout();
                String message = "Publication '" + model.getTitle() + "' was created in series '" + model.getSeries() + "'";
                log.info(message);
                return Response.ok(new PublicationResponse(message)).build();
            } catch (RepositoryException | ObjectBeanManagerException ex) {
                authenticatedSession.logout();
                String message = "Exception while creating publication with title '" + model.getTitle() + "'";
                log.error(message, ex);
                return Response.serverError().entity(new PublicationResponse(message)).build();
            }
        } else {
            authenticatedSession.logout();
            String message = "There have been too many request recently, please wait a few seconds before retry";
            log.error(message);
            return Response.status(Response.Status.TOO_MANY_REQUESTS.getStatusCode()).entity(new PublicationResponse(message)).build();
        }
    }

    private Session authenticateUser(String authorization) throws RepositoryException {
        String[] usernameAndPassword = extractUsernameAndPassword(authorization);
        Repository repository = HstServices
                .getComponentManager()
                .getComponent(Repository.class.getName() + ".delegating");
        Credentials credentials = new SimpleCredentials(usernameAndPassword[0], usernameAndPassword[1].toCharArray());
        return repository.login(credentials);
    }

    private String[] extractUsernameAndPassword(String authorization) {
        log.debug("authorization=" + authorization);
        return Base64.decode(authorization.split(" ")[1]).split(":");
    }

    private String findPathToSeries(Session session, String seriesTitle) throws RepositoryException {
        QueryManager queryManager = session.getWorkspace().getQueryManager();
        QueryResult queryResult = queryManager.createQuery(
                "/jcr:root/content//*[(@jcr:primaryType='publicationsystem:series') and (@publicationsystem:Title='" + seriesTitle + "')]", Query.XPATH).execute();
        final NodeIterator nodes = queryResult.getNodes();
        String path = "";
        if (nodes.hasNext()) {
            Node nodeSeries = nodes.nextNode();
            if (nodeSeries != null) {
                Node nodeHippoHandle = nodeSeries.getParent();
                if (nodeHippoHandle != null) {
                    Node seriesFolderNode = nodeHippoHandle.getParent();
                    if (seriesFolderNode != null) {
                        path = seriesFolderNode.getPath();
                    }
                }
            }
        }
        log.debug("path to series titled='" + seriesTitle + "' is '" + path + "'");
        return path;
    }

    private void writePublication(HstRequestContext rc, Session session, String pathToSeriesFolder, PublicationModel model) throws ObjectBeanManagerException, RepositoryException {
        Publication publicationBean;
        WorkflowPersistenceManager wpm = (WorkflowPersistenceManager) getPersistenceManager(rc, session);
        String publicationFolderPath = pathToSeriesFolder;
        String beanPath = wpm.createAndReturn(publicationFolderPath, PUBLICATION_DOCTYPE, model.getTitle(), true);
        publicationBean = (Publication) wpm.getObject(beanPath);
        publicationBean.setTitle(model.getTitle());
        wpm.update(publicationBean);
        wpm.save();
    }
}
