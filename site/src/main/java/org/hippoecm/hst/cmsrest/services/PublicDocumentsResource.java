package org.hippoecm.hst.cmsrest.services;

import org.hippoecm.hst.configuration.hosting.*;
import org.hippoecm.hst.container.*;
import org.hippoecm.hst.core.linking.*;
import org.slf4j.*;

/**
 * Copy of the DocumentResource class changing the definition of the getUrl method.
 * This class mainly a wrapper of the parent one since couple of useful methods can
 * be reused.
 */
public class PublicDocumentsResource extends DocumentsResource {

    private static final Logger log = LoggerFactory.getLogger(PublicDocumentsResource.class);

    /**
     * Setting the link creator in the parent class
     *
     * @param hstLinkCreator coming from the documentRestPlainResourceProvider.xml
     */
    @Override
    public void setHstLinkCreator(HstLinkCreator hstLinkCreator) {
        if (hstLinkCreator != null) {
            super.setHstLinkCreator(hstLinkCreator);
        }
    }

    @Override
    public String getHostGroupNameForCmsHost() {
        //returning the host group name matching the request
        return RequestContextProvider.get().getVirtualHost().getHostGroupName();
    }

    /**
     * Parent definition is 'replacing' the mount property of the resolved mount
     * object with a undecorated one since it's expecting the request from the cms.
     * This different implementation is skipping this extra action.
     *
     * @param uuid of the handle node
     * @param type of document availability (e.g. live)
     * @return String containing the canonical url of the document
     */
    @Override
    public String getUrl(final String uuid, final String type) {

        if (Mount.LIVE_NAME.equals(type)) {
            //using the getBestLink definition from the parent node
            HstLink bestLink = getBestLink(uuid, type);
            if (bestLink == null) {
                return "";
            }

            return bestLink.toUrlForm(RequestContextProvider.get(), true);
        } else {
            log.warn("Type provided '{}' for document with UUID '{}' is not supported", type, uuid);
            return "";
        }

    }
}
