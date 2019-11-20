package uk.nhs.digital.common.valves;

import org.hippoecm.hst.content.beans.query.*;
import org.hippoecm.hst.content.beans.query.exceptions.*;
import org.hippoecm.hst.content.beans.query.filter.*;
import org.hippoecm.hst.core.request.*;
import org.slf4j.*;


public class ArticleValve extends AbstractDocumentIdValve {

    @Override
    public String getBasePath() {
        return "/article/";
    }

    @Override
    public String getDocumentIdFromUrl(final String pathInfo, final String basePath) {
        if (pathInfo.matches(basePath + "\\d+")
            || pathInfo.matches(basePath + "\\d+/.*")) {
            return pathInfo.split(basePath)[1].split("/")[0];
        }
        return null;
    }

    @Override
    public HstQuery getQuery(final HstRequestContext requestContext, final String documentId) throws QueryException {
        HstQuery hstQuery = requestContext.getQueryManager()
            .createQuery(requestContext.getSiteContentBaseBean(),
                "website:service",
                "website:general",
                "website:hub",
                "website:componentlist",
                "publicationsystem:legacypublication");
        //creating the goss id for the website documents
        Filter gossidFilter = hstQuery.createFilter();
        hstQuery.setFilter(gossidFilter);
        gossidFilter.addEqualTo("website:gossid", documentId);
        //creating a different filter for the legacy publication document (different namespace)
        Filter publicationFilter = hstQuery.createFilter();
        publicationFilter.addEqualTo("publicationsystem:gossid", documentId);
        //checking goss id attribute value either for website OR publicationsystem documents
        gossidFilter.addOrFilter(publicationFilter);

        return hstQuery;
    }
}

