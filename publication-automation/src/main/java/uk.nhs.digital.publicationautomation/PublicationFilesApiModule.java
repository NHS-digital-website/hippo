package uk.nhs.digital.publicationautomation;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.repository.jaxrs.RepositoryJaxrsEndpoint;
import org.onehippo.repository.jaxrs.RepositoryJaxrsService;
import org.onehippo.repository.modules.DaemonModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.externalstorage.s3.PooledS3Connector;
import uk.nhs.digital.externalstorage.s3.S3ObjectMetadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class PublicationFilesApiModule implements DaemonModule {

    private static final Logger log = LoggerFactory.getLogger(PublicationFilesApiModule.class);

    protected Session session;

    @Override
    public void initialize(final Session session) throws RepositoryException {
        RepositoryJaxrsEndpoint endpoint = new RepositoryJaxrsEndpoint("/publication")
                .singleton(new PublicationFilesResource())
                .singleton(new JacksonJsonProvider());
        RepositoryJaxrsService.addEndpoint(endpoint);
        this.session = session;
    }

    @Override
    public void shutdown() {
        RepositoryJaxrsService.removeEndpoint("/publication");
    }

    public class PublicationFilesResource {

        @Path("/files")
        @POST
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.MULTIPART_FORM_DATA)
        public Response post(@Multipart(value = "files") List<Attachment> attachments) {
            PublicationFilesResponse response = new PublicationFilesResponse();

            List fileNamesUploaded = new ArrayList<String>();
            List s3Urls = new ArrayList<String>();
            List errors = new ArrayList<String>();
            for (Attachment attachment : attachments) {
                try {
                    S3ObjectMetadata metadata = handleUpload(attachment);
                    fileNamesUploaded.add(metadata.getFileName());
                    s3Urls.add(metadata.getUrl());
                    log.info("File '" + metadata.getUrl() + "' was uploaded successfully");
                } catch (Exception ex) {
                    String message = "There was a problem uploading file '" + attachment.getDataHandler().getName();
                    errors.add(message);
                    log.error(message, ex);
                }
            }
            response.setFileNames(fileNamesUploaded);
            response.setS3Urls(s3Urls);
            response.setErrors(errors);
            if (response.getErrors().isEmpty()) {
                return Response.ok(response).build();
            } else {
                return Response.serverError().build();
            }
        }
    }

    private S3ObjectMetadata handleUpload(Attachment attachment) throws IOException {
        String filename = attachment.getDataHandler().getName();
        String mimeType = attachment.getDataHandler().getContentType();
        InputStream inputFile = attachment.getDataHandler().getDataSource().getInputStream();

        final PooledS3Connector s3Connector = HippoServiceRegistry.getService(PooledS3Connector.class);
        Supplier<InputStream> inputStreamSupplier = new Supplier<InputStream>() {
            @Override
            public InputStream get() {
                return inputFile;
            }
        };
        final S3ObjectMetadata s3ObjectMetadata = s3Connector.upload(
                inputStreamSupplier,
                filename,
                mimeType
        );

        log.debug("s3ObjectMetadata.fileName= " + s3ObjectMetadata.getFileName());
        log.debug("s3ObjectMetadata.mimeType= " + s3ObjectMetadata.getMimeType());
        log.debug("s3ObjectMetadata.url= " + s3ObjectMetadata.getUrl());
        log.debug("s3ObjectMetadata.size= " + s3ObjectMetadata.getSize());
        return s3ObjectMetadata;
    }
}
