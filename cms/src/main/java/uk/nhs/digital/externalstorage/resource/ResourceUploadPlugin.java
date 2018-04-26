package uk.nhs.digital.externalstorage.resource;

import static uk.nhs.digital.externalstorage.s3.PooledS3Connector.wrapCheckedException;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.hippoecm.frontend.behaviors.EventStoppingBehavior;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.plugins.jquery.upload.FileUploadViolationException;
import org.hippoecm.frontend.plugins.jquery.upload.single.FileUploadPanel;
import org.hippoecm.frontend.plugins.yui.upload.validation.DefaultUploadValidationService;
import org.hippoecm.frontend.plugins.yui.upload.validation.FileUploadValidationService;
import org.hippoecm.frontend.service.IEditor;
import org.hippoecm.frontend.service.render.RenderPlugin;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.externalstorage.ExternalStorageConstants;
import uk.nhs.digital.externalstorage.s3.PooledS3Connector;
import uk.nhs.digital.externalstorage.s3.S3ObjectMetadata;

import java.util.Calendar;
import javax.jcr.Node;
import javax.jcr.RepositoryException;


/**
 * Plugin for uploading resources into the JCR repository.
 * This plugin can be configured with specific types, so not all file types are allowed to be uploaded.
 */
public class ResourceUploadPlugin extends RenderPlugin {

    static final Logger log = LoggerFactory.getLogger(ResourceUploadPlugin.class);
    public static final String DEFAULT_ASSET_VALIDATION_SERVICE_ID = "service.gallery.asset.validation";

    private final IEditor.Mode mode;

    public ResourceUploadPlugin(IPluginContext context, IPluginConfig config) {
        super(context, config);
        mode = IEditor.Mode.fromString(config.getString("mode"), IEditor.Mode.EDIT);
        add(createFileUploadPanel());
        add(new EventStoppingBehavior("onclick"));
    }

    private FileUploadPanel createFileUploadPanel() {
        final FileUploadPanel panel = new FileUploadPanel("fileUpload", getPluginConfig(), getValidationService()) {
            @Override
            public void onFileUpload(final FileUpload fileUpload) throws FileUploadViolationException {
                handleUpload(fileUpload);
            }
        };
        panel.setVisible(mode == IEditor.Mode.EDIT);
        return panel;
    }

    private FileUploadValidationService getValidationService() {
        return DefaultUploadValidationService.getValidationService(getPluginContext(), getPluginConfig(),
            DEFAULT_ASSET_VALIDATION_SERVICE_ID);
    }

    /**
     * Handles the file upload from the form.
     *
     * @param upload the {@link FileUpload} containing the upload information
     */
    private void handleUpload(FileUpload upload) throws FileUploadViolationException {
        final PooledS3Connector s3Connector = HippoServiceRegistry.getService(PooledS3Connector.class);

        String fileName = upload.getClientFileName();
        String mimeType = upload.getContentType();

        try {
            final S3ObjectMetadata s3ObjectMetadata = s3Connector.upload(
                wrapCheckedException(upload::getInputStream),
                fileName,
                mimeType
            );

            JcrNodeModel nodeModel = (JcrNodeModel) this.getDefaultModel();
            Node node = nodeModel.getNode();
            try {
                setResourceProperties(node, s3ObjectMetadata);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        } catch (Exception ex) {
            log.error("Cannot upload resource", ex);
            throw new FileUploadViolationException(ex.getMessage());
        }
    }

    private void setResourceProperties(Node node, S3ObjectMetadata metadata) throws RepositoryException {
        node.setProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_LAST_MODIFIED, Calendar.getInstance());
        node.setProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_FILE_NAME, metadata.getFileName());
        node.setProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_SIZE, metadata.getSize());
        node.setProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_REFERENCE, metadata.getReference());
        node.setProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_PUBLIC_URL, metadata.getUrl());
        node.setProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_MIME_TYPE, metadata.getMimeType());
    }

}
