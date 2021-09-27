package uk.nhs.digital.freemarker.svg;

import static org.hippoecm.hst.site.HstServices.getComponentManager;

import uk.nhs.digital.freemarker.AbstractRemoteContent;

public class SvgFromUrl extends AbstractRemoteContent {

    private static final String RESOURCE_RESOLVER = "svgResourceResolver";

    public SvgFromUrl() {
        super(RESOURCE_RESOLVER, SvgContent.class, getComponentManager().getComponent("basicRemoteContentService"));
    }

}
