package uk.nhs.digital.common.valves;

import org.hippoecm.hst.content.beans.query.*;
import org.hippoecm.hst.content.beans.query.exceptions.*;
import org.hippoecm.hst.content.beans.query.filter.*;
import org.hippoecm.hst.core.request.*;


public class PublicationValve extends AbstractDocumentIdValve {

    @Override
    public String getBasePath() {
        return "/catalogue/";
    }

    @Override
    public String getDocumentIdFromUrl(final String pathInfo, final String basePath) {
        if (pathInfo.matches(basePath + "[a-zA-Z0-9]+")
            || pathInfo.matches(basePath + "[a-zA-Z0-9]+/.*")) {
            return pathInfo.split(basePath)[1].split("/")[0];
        }
        return null;
    }

    @Override
    public HstQuery getQuery(final HstRequestContext requestContext, final String documentId) throws QueryException {
        HstQuery hstQuery = requestContext.getQueryManager()
            .createQuery(requestContext.getSiteContentBaseBean(),
                "publicationsystem:legacypublication");
        //creating the goss id for the website documents
        Filter publicationidFilter = hstQuery.createFilter();
        hstQuery.setFilter(publicationidFilter);
        publicationidFilter.addContains("publicationsystem:publicationid", documentId);

        return hstQuery;
    }
}

