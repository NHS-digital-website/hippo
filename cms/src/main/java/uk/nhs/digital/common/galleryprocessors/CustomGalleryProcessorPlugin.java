package uk.nhs.digital.common.galleryprocessors;

import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.Plugin;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.plugins.gallery.NullGalleryProcessorPlugin;
import org.hippoecm.frontend.plugins.gallery.model.GalleryProcessor;

public class CustomGalleryProcessorPlugin extends Plugin {

    public CustomGalleryProcessorPlugin(IPluginContext context, IPluginConfig config) {
        super(context, config);

        CustomGalleryProcessor processor = new CustomGalleryProcessor();
        context.registerService(processor, config.getString(GalleryProcessor.GALLERY_PROCESSOR_ID,
                NullGalleryProcessorPlugin.DEFAULT_ASSET_GALLERY_PROCESSOR_SERVICE_ID));
    }

}
