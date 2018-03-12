package uk.nhs.digital.externalstorage.resource;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.hippoecm.frontend.model.IModelReference;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.plugins.standards.util.ByteSizeFormatter;
import org.hippoecm.frontend.service.IEditor;
import org.hippoecm.frontend.service.render.RenderPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.externalstorage.ExternalStorageConstants;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class ImageDisplayPlugin extends RenderPlugin<Node> {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(ImageDisplayPlugin.class);

    public static final String MIME_TYPE_HIPPO_BLANK = "application/vnd.hippo.blank";

    ByteSizeFormatter formatter = new ByteSizeFormatter();

    public ImageDisplayPlugin(IPluginContext context, IPluginConfig config) {
        super(context, config);

        IEditor.Mode mode = IEditor.Mode.fromString(config.getString("mode"), IEditor.Mode.VIEW);
        if (mode == IEditor.Mode.COMPARE && config.containsKey("model.compareTo")) {
            IModelReference<Node> baseModelRef = context.getService(config.getString("model.compareTo"),
                IModelReference.class);
            boolean doCompare = false;
            if (baseModelRef != null) {
                IModel<Node> baseModel = baseModelRef.getModel();
                Node baseNode = baseModel.getObject();
                Node currentNode = getModel().getObject();
                if (baseNode != null && currentNode != null) {
                    try {
                        String baseNodeReference = baseNode.getProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_REFERENCE).getString();
                        String currentNodeReference = currentNode.getProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_REFERENCE).getString();
                        doCompare = baseNodeReference != currentNodeReference;
                    } catch (RepositoryException ex) {
                        log.error("Could not compare references", ex);
                    }
                }
            }
            if (doCompare) {
                Fragment fragment = new Fragment("fragment", "compare", this);
                Fragment baseFragment = createResourceFragment("base", baseModelRef.getModel());
                baseFragment.add(new AttributeAppender("class", new Model<String>("hippo-diff-removed"), " "));
                fragment.add(baseFragment);

                Fragment currentFragment = createResourceFragment("current", getModel());
                currentFragment.add(new AttributeAppender("class", new Model<String>("hippo-diff-added"), " "));
                fragment.add(currentFragment);
                add(fragment);
            } else {
                add(createResourceFragment("fragment", getModel()));
            }
        } else {
            add(createResourceFragment("fragment", getModel()));
        }
    }

    private Fragment createResourceFragment(String id, IModel<Node> model) {
        S3NodeMetadata metadata = new S3NodeMetadata(model.getObject());
        Fragment fragment = new Fragment(id, "unknown", this);

        if (metadata.getSize() < 0) {
            return fragment;
        }

        fragment = new Fragment(id, "embed", this);
        fragment.add(new Label("filesize", Model.of(formatter.format(metadata.getSize()))));
        fragment.add(new Label("mimetype", Model.of(metadata.getMimeType())));

        if (!metadata.getFileName().isEmpty()) {
            fragment.add(new Label("filename", new Model<String>(metadata.getFileName())));
        }

        if (metadata.getMimeType().equals(MIME_TYPE_HIPPO_BLANK)) {
            fragment.setVisible(false);
        }

        return fragment;
    }

    @Override
    protected void onModelChanged() {
        replace(createResourceFragment("fragment", getModel()));
        super.onModelChanged();
        redraw();
    }
}
