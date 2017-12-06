package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.request.HstRequestContext;

import javax.jcr.RepositoryException;

/**
 * Static helper for {@linkplain org.hippoecm.hst.content.beans.standard.HippoBean}
 */
public class HippoBeanHelper {

    public static boolean isRootFolder(HippoBean folder) {
        HippoBean siteContentBaseBean = RequestContextProvider.get().getSiteContentBaseBean();

        return folder.isSelf(siteContentBaseBean);
    }

    public static String getTaxonomyName() throws HstComponentException {
        String taxonomyName;

        try {
            HstRequestContext ctx = RequestContextProvider.get();
            taxonomyName = ctx.getSession().getNode(
                "/hippo:namespaces/publicationsystem/publication/editor:templates/_default_/classifiable")
                .getProperty("essentials-taxonomy-name")
                .getString();
        } catch (RepositoryException e) {
            throw new HstComponentException(
                "Exception occurred during fetching taxonomy file name.", e);
        }

        return taxonomyName;
    }
}
