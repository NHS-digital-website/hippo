package uk.nhs.digital.website.beans;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:projectfeed")
@Node(jcrType = "website:projectfeed")
public class ProjectFeed extends CommonFieldsBean {

    private static final String EARLY_ACCESS_KEY_QUERY_PARAM = "key";

    @HippoEssentialsGenerated(internalName = "website:optionalIntroductoryText", allowModifications = false)
    public HippoHtml getOptionalIntroductoryText() {
        return getHippoHtml("website:optionalIntroductoryText");
    }

    public boolean isCorrectAccessKey() {
        return StringUtils.isNotBlank(getProperty("website:earlyaccesskey"))
            && getProperty("website:earlyaccesskey").equals(
            RequestContextProvider.get().getServletRequest()
                .getParameter(EARLY_ACCESS_KEY_QUERY_PARAM));
    }
}
