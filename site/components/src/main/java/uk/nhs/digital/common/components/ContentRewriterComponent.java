package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.*;
import org.onehippo.cms7.essentials.components.*;
import uk.nhs.digital.common.contentrewriters.BrandRefreshContentRewriter;
import uk.nhs.digital.common.contentrewriters.GoogleAnalyticsContentRewriter;
import uk.nhs.digital.common.contentrewriters.StripTagsContentRewriter;
import uk.nhs.digital.common.contentrewriters.StripTagsWithLinksContentRewriter;

/**
 * A centralised component for injecting ContentRewriters into FTLs that are manageable
 * via component properties in the Console at runtime or YAML.
 */
public class ContentRewriterComponent extends EssentialsContentComponent {

    private static final BrandRefreshContentRewriter brContentRewriter = new BrandRefreshContentRewriter();
    private static final GoogleAnalyticsContentRewriter gaContentRewriter = new GoogleAnalyticsContentRewriter();
    private static final StripTagsContentRewriter stripTagsContentRewriter = new StripTagsContentRewriter();
    private static final StripTagsWithLinksContentRewriter stripTagsWithLinksContentRewriter = new StripTagsWithLinksContentRewriter();

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        if (useBrandRefreshContentRewriter()) {
            request.setAttribute("brContentRewriter", brContentRewriter);
        }
        if (useStripTagsContentRewriter()) {
            request.setAttribute("stripTagsContentRewriter", stripTagsContentRewriter);
        }
        if (useStripTagsWithLinksContentRewriter()) {
            request.setAttribute("stripTagsWithLinksContentRewriter", stripTagsWithLinksContentRewriter);
        }
        if (useGoogleAnalyticsContentRewriter()) {
            request.setAttribute("gaContentRewriter", gaContentRewriter);
        }
    }

    private boolean useBrandRefreshContentRewriter() {
        return useContentRewriter("brandRefreshContentRewriter", false);
    }

    private boolean useStripTagsContentRewriter() {
        return useContentRewriter("stripTagsContentRewriter", false);
    }

    private boolean useStripTagsWithLinksContentRewriter() {
        return useContentRewriter("stripTagsWithLinksContentRewriter", false);
    }

    private boolean useGoogleAnalyticsContentRewriter() {
        return useContentRewriter("googleAnalyticsContentRewriter", true);
    }

    private boolean useContentRewriter(String property, boolean defaultValue) {
        if (getComponentConfiguration() != null && getComponentParameter(property) != null) {
            return Boolean.parseBoolean(getComponentParameter(property));
        }
        return defaultValue;
    }

}
