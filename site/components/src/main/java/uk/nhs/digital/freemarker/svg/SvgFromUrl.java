package uk.nhs.digital.freemarker.svg;

import static org.hippoecm.hst.site.HstServices.getComponentManager;

import uk.nhs.digital.freemarker.AbstractRemoteContent;
import uk.nhs.digital.svg.SvgProvider;

/**
 * @deprecated
 * For future SVG usage, this class should not be used. Please upload svg images on the repository.
 * <p> Use {@link SvgProvider#getSvgXmlFromBean(org.hippoecm.hst.content.beans.standard.HippoBean)} instead.
 */
@Deprecated
public class SvgFromUrl extends AbstractRemoteContent {

    private static final String RESOURCE_RESOLVER = "svgResourceResolver";

    public SvgFromUrl() {
        super(RESOURCE_RESOLVER, SvgContent.class, getComponentManager().getComponent("basicRemoteContentService"));
    }

}
