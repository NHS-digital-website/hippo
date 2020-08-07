package uk.nhs.digital.common.processor.asset;

import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.Plugin;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.plugins.gallery.NullGalleryProcessorPlugin;
import org.hippoecm.frontend.plugins.gallery.model.GalleryProcessor;
import uk.nhs.digital.common.processor.asset.galleryprocessors.MyNullGalleryProcessor;
import uk.nhs.digital.common.processor.asset.svgstyle.SvgStyleGalleryProcessor;

public class AssetProcessorServicesPlugins extends Plugin {

    public AssetProcessorServicesPlugins(IPluginContext context, IPluginConfig config) {
        super(context, config);

        // Register MyNullGalleryProcessor
        MyNullGalleryProcessor nullProcessor = new MyNullGalleryProcessor();
        context.registerService(nullProcessor, config.getString(GalleryProcessor.GALLERY_PROCESSOR_ID,
                NullGalleryProcessorPlugin.DEFAULT_ASSET_GALLERY_PROCESSOR_SERVICE_ID));

        // Register SvgStyleGalleryProcessor
        SvgStyleGalleryProcessor svgProcessor = new SvgStyleGalleryProcessor();
        context.registerService(svgProcessor, config.getString(SvgStyleGalleryProcessor.DEFAULT_SVG_ASSET_STYLESHEET_URL));
    }

}
