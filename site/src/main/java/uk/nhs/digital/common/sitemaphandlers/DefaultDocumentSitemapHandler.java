package uk.nhs.digital.common.sitemaphandlers;

import org.hippoecm.hst.configuration.components.*;
import org.hippoecm.hst.configuration.sitemap.*;
import org.hippoecm.hst.container.*;
import org.hippoecm.hst.content.beans.standard.*;
import org.hippoecm.hst.core.request.*;
import org.hippoecm.hst.core.sitemapitemhandler.*;

import java.util.*;
import javax.servlet.http.*;

public class DefaultDocumentSitemapHandler extends AbstractHstSiteMapItemHandler {

    @Override
    public ResolvedSiteMapItem process(final ResolvedSiteMapItem resolvedSiteMapItem,
                                       final HttpServletRequest httpServletRequest,
                                       final HttpServletResponse httpServletResponse) throws HstSiteMapItemHandlerException {
        HstRequestContext requestContext = RequestContextProvider.get();

        if (requestContext.getContentBean() instanceof HippoFolder) {

            return new ResolvedSiteMapItem() {
                @Override
                public ResolvedMount getResolvedMount() {
                    return resolvedSiteMapItem.getResolvedMount();
                }

                @Override
                public String getRelativeContentPath() {
                    String relativeContentPath = resolvedSiteMapItem.getRelativeContentPath();
                    String[] relativeContentPathTokens = relativeContentPath.split("/");
                    String fileName = relativeContentPathTokens[relativeContentPathTokens.length - 1];

                    return relativeContentPath + "/" + fileName;
                }

                @Override
                public String getPathInfo() {
                    return resolvedSiteMapItem.getPathInfo();
                }

                @Override
                public String getPageTitle() {
                    return resolvedSiteMapItem.getPageTitle();
                }

                @Override
                public String getParameter(final String name) {
                    return resolvedSiteMapItem.getParameter(name);
                }

                @Override
                public String getLocalParameter(final String name) {
                    return resolvedSiteMapItem.getLocalParameter(name);
                }

                @Override
                public Properties getParameters() {
                    return resolvedSiteMapItem.getParameters();
                }

                @Override
                public Properties getLocalParameters() {
                    return resolvedSiteMapItem.getLocalParameters();
                }

                @Override
                public HstSiteMapItem getHstSiteMapItem() {
                    return resolvedSiteMapItem.getHstSiteMapItem();
                }

                @Override
                public String getNamedPipeline() {
                    return resolvedSiteMapItem.getNamedPipeline();
                }

                @Override
                public int getStatusCode() {
                    return resolvedSiteMapItem.getStatusCode();
                }

                @Override
                public int getErrorCode() {
                    return resolvedSiteMapItem.getErrorCode();
                }

                @Override
                public boolean isAuthenticated() {
                    return resolvedSiteMapItem.isAuthenticated();
                }

                @Override
                public Set<String> getRoles() {
                    return resolvedSiteMapItem.getRoles();
                }

                @Override
                public Set<String> getUsers() {
                    return resolvedSiteMapItem.getUsers();
                }

                @Override
                public HstComponentConfiguration getHstComponentConfiguration() {
                    return resolvedSiteMapItem.getHstComponentConfiguration();
                }
            };
        }
        return resolvedSiteMapItem;
    }

}
