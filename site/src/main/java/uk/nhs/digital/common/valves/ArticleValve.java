package uk.nhs.digital.common.valves;

import org.hippoecm.hst.container.valves.AbstractOrderableValve;
import org.hippoecm.hst.content.beans.query.*;
import org.hippoecm.hst.content.beans.query.exceptions.*;
import org.hippoecm.hst.content.beans.query.filter.*;
import org.hippoecm.hst.core.container.*;
import org.hippoecm.hst.core.linking.*;
import org.hippoecm.hst.core.request.*;
import org.slf4j.*;

import java.io.*;


public class ArticleValve extends AbstractOrderableValve {

    private static final Logger log = LoggerFactory.getLogger(ArticleValve.class);

    @Override
    public void invoke(ValveContext context) throws ContainerException {
        try {
            HstRequestContext requestContext = context.getRequestContext();
            //fetching the path info
            String pathInfo = requestContext.getServletRequest().getPathInfo();
            //intercepting requests having article in the url
            if (pathInfo.matches("/article/\\d+")
                || pathInfo.matches("/article/\\d+/.*")) {
                try {
                    //extracting the gossid from the url
                    Integer gossId = new Integer(pathInfo.split("/article/")[1].split("/")[0]);
                    HstQuery hstQuery = requestContext.getQueryManager()
                        .createQuery(requestContext.getSiteContentBaseBean(),
                            "website:service",
                            "website:general",
                            "website:hub",
                            "website:componentlist",
                            "publicationsystem:legacypublication");
                    hstQuery.setLimit(1);
                    //creating the goss id for the website documents
                    Filter gossidFilter = hstQuery.createFilter();
                    hstQuery.setFilter(gossidFilter);
                    gossidFilter.addEqualTo("website:gossid", gossId);
                    //creating a different filter for the legacy publication document (different namespace)
                    Filter publicationFilter = hstQuery.createFilter();
                    publicationFilter.addEqualTo("publicationsystem:gossid", gossId);
                    //checking goss id attribute value either for website OR publicationsystem documents
                    gossidFilter.addOrFilter(publicationFilter);

                    HstQueryResult result = hstQuery.execute();
                    //if at least one bean has been found with that gossid, then a redirect is needed
                    if (result.getSize() > 0) {
                        // inkCreator is responsible for link generation in the site application
                        HstLinkCreator linkCreator = requestContext.getHstLinkCreator();
                        // fetching the canonical url related to the retrieved bean
                        HstLink link = linkCreator.create(result.getHippoBeans().nextHippoBean(), requestContext);
                        //in case the document is served in a non root channel, add the channel name to the url
                        String channelName = link.getMount().getMountPath();
                        //redirecting the user to the new url, taking into consideration the servlet context + channel path and it's trailing slash
                        requestContext.getServletResponse().sendRedirect(requestContext.getBaseURL().getContextPath()
                            + channelName + "/" + link.getPath());
                    }
                } catch (QueryException queryExcpetion) {
                    log.error("Query exception while creating query having scope {} {}", requestContext.getSiteContentBaseBean(), queryExcpetion);
                } catch (IOException ioExcpetion) {
                    log.error("UI exception while redirecting {}", ioExcpetion);
                }
            }
        } finally {
            context.invokeNext();
        }
    }

}

