package uk.nhs.digital.website.rest;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.annotations.Persistable;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.manager.workflow.WorkflowPersistenceManager;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.onehippo.cms7.essentials.components.rest.BaseRestResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.beans.Publication;

import java.time.Duration;

import javax.jcr.RepositoryException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class PublicationApiResource extends BaseRestResource {

    private static final Logger log = LoggerFactory.getLogger(PublicationApiResource.class);

    private static final String PUBLICATION_DOCTYPE = "publicationsystem:publication";

    private final Bucket bucket; // for rate limiting (throttling)

    public PublicationApiResource() {
        bucket = Bucket4j.builder()
            .addLimit(Bandwidth.classic(3, Refill.intervally(3, Duration.ofMinutes(1)))) //3 requests per minute for POC purposes.Not an actually good number.
            .build();
    }

    /**
     * Example usage:
     * POST http://localhost:8080/site/restapi/publication
     * payload:
     * {
     *     "path": "publication-system/pocfolder",
     *     "title": "Test"
     * }
     */

    @Path("/publication")
    @POST
    @Persistable
    @Produces({MediaType.APPLICATION_JSON})
    public Response postPublication(PublicationModel model) {
        if (bucket.tryConsume(1)) {
            final HstRequestContext rc = RequestContextProvider.get();
            Publication publicationBean;
            try {
                WorkflowPersistenceManager wpm = (WorkflowPersistenceManager) getPersistenceManager(rc);
                String publicationFolderPath = "/" + rc.getSiteContentBasePath() + "/" + model.getPath();
                String beanPath = wpm.createAndReturn(publicationFolderPath, PUBLICATION_DOCTYPE, model.getTitle(), true);
                publicationBean = (Publication) wpm.getObject(beanPath);
                publicationBean.setTitle(model.getTitle());
                wpm.update(publicationBean);
                wpm.save();
            } catch (RepositoryException | ObjectBeanManagerException ex) {
                log.error("Exception while creating document", ex);
            }
            return Response.ok("success").build();
        }
        return Response.status(Response.Status.TOO_MANY_REQUESTS).build();
    }
}
