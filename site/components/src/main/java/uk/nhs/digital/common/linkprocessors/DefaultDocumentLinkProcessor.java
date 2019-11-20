package uk.nhs.digital.common.linkprocessors;

import org.apache.commons.lang3.*;
import org.hippoecm.hst.core.linking.*;
import org.hippoecm.hst.linking.*;

/**
 * The content organization proposed for the Digital Website, in regards to service
 * documents, has the following structure:
 *
 * - Service1 (Folder)
 * |
 * |_ Service1 (Document)
 *
 * By default, CMS will show this URL under this URL: "base_path/service1/service1"
 * This link processor, combined with the DefaultDocumentSitemapHandler, is creating
 * a link to that actually skip the last token ("service1"). When the CMS is
 * producing links for the service1 document then will be: "base_path/service1"
 */
public class DefaultDocumentLinkProcessor extends HstLinkProcessorTemplate {

    @Override
    protected HstLink doPreProcess(final HstLink hstLink) {
        return hstLink;
    }

    @Override
    protected HstLink doPostProcess(HstLink hstLink) {
        String path = hstLink.getPath();
        String[] pathTokens = path.split("/");
        int tokenLength = pathTokens.length;
        if (tokenLength > 1 && pathTokens[tokenLength - 1].equals("content")) {
            //removing the last token to avoid repetition
            String shortPath = StringUtils.join(pathTokens, "/", 0, tokenLength - 1);
            hstLink.setPath(shortPath);
        }
        return hstLink;
    }
}
