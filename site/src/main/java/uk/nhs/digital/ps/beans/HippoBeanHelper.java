package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;

/**
 * Static helper for {@linkplain org.hippoecm.hst.content.beans.standard.HippoBean}
 */
public class HippoBeanHelper {

    public static boolean isRootFolder(HippoBean folder) {
        HippoBean siteContentBaseBean = RequestContextProvider.get().getSiteContentBaseBean();

        return folder.isSelf(siteContentBaseBean);
    }
}
