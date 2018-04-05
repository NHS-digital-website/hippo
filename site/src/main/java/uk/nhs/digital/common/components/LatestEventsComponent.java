package uk.nhs.digital.common.components;

import com.google.common.base.*;
import org.hippoecm.hst.content.beans.*;
import org.hippoecm.hst.content.beans.manager.*;
import org.hippoecm.hst.content.beans.query.*;
import org.hippoecm.hst.content.beans.query.exceptions.*;
import org.hippoecm.hst.content.beans.query.filter.*;
import org.hippoecm.hst.content.beans.standard.*;
import org.hippoecm.hst.core.component.*;
import org.hippoecm.hst.core.parameters.*;
import org.hippoecm.hst.core.request.*;
import org.hippoecm.repository.util.*;
import org.onehippo.cms7.essentials.components.*;
import org.onehippo.cms7.essentials.components.info.*;
import org.onehippo.cms7.essentials.components.paging.*;
import org.slf4j.*;
import uk.nhs.digital.common.components.info.*;

import java.util.*;
import javax.jcr.*;
import javax.jcr.Node;
import javax.jcr.query.*;

@ParametersInfo(
    type = LatestEventsComponentInfo.class
)
public class LatestEventsComponent extends EssentialsEventsComponent {

    private static Logger log = LoggerFactory.getLogger(LatestEventsComponent.class);

    @Override
    protected <T extends EssentialsListComponentInfo> Pageable<HippoBean> executeQuery(HstRequest request, T paramInfo, HstQuery query) throws QueryException {
        int pageSize = this.getPageSize(request, paramInfo);
        int page = this.getCurrentPage(request);
        query.setLimit(pageSize);
        query.setOffset((page - 1) * pageSize);
        this.applyOrdering(request, query, paramInfo);
        this.applyExcludeScopes(request, query, paramInfo);
        this.buildAndApplyFilters(request, query);

        try {
            String queryString = query.getQueryAsString(true);
            queryString = queryString.replace("@hippo:paths", "../@hippo:paths");
            queryString = queryString.replace("@jcr:primaryType", "../@jcr:primaryType");
            queryString = queryString.replace("@hippo:availability", "../@hippo:availability");

            HstRequestContext requestContext = request.getRequestContext();
            QueryManager jcrQueryManager = requestContext.getSession().getWorkspace().getQueryManager();
            Query jcrQuery = jcrQueryManager.createQuery(queryString, "xpath");
            QueryResult queryResult = jcrQuery.execute();

            ObjectConverter objectConverter = requestContext.getContentBeansTool().getObjectConverter();

            NodeIterator it = queryResult.getNodes();
            List parentNodes = new ArrayList();
            List<String> parentPath = new ArrayList();
            while (it.hasNext()) {
                Node interval = it.nextNode();
                Node eventNode = interval.getParent();
                if (eventNode.getPrimaryNodeType().isNodeType("website:event")
                    && !parentPath.contains(eventNode.getPath())) {
                    parentPath.add(eventNode.getPath());
                    parentNodes.add(objectConverter.getObject(eventNode));
                }
            }

            return this.getPageableFactory().createPageable(parentNodes, page, pageSize);
        } catch (RepositoryException repositoryEx) {
            throw new QueryException(repositoryEx.getMessage());
        } catch (ObjectBeanManagerException converterEx) {
            throw new QueryException(converterEx.getMessage());
        }
    }

    @Override
    protected void contributeAndFilters(final List<BaseFilter> filters, final HstRequest request, final HstQuery query) {
        final EssentialsEventsComponentInfo paramInfo = getComponentParametersInfo(request);
        if (paramInfo.getHidePastEvents()) {
            final String dateField = paramInfo.getDocumentDateField();
            if (!Strings.isNullOrEmpty(dateField)) {
                try {
                    final Filter upcomingEventsFilter = query.createFilter();
                    upcomingEventsFilter.addGreaterOrEqualThan(dateField, Calendar.getInstance(), DateTools.Resolution.HOUR);
                    Calendar today = Calendar.getInstance();
                    //including the currently running events
                    final Filter runningEventsFilter = query.createFilter();
                    runningEventsFilter.addLessOrEqualThan(dateField, today, DateTools.Resolution.HOUR);
                    runningEventsFilter.addGreaterOrEqualThan("website:enddatetime", today, DateTools.Resolution.HOUR);
                    upcomingEventsFilter.addOrFilter(runningEventsFilter);
                    filters.add(upcomingEventsFilter);
                } catch (FilterException filterException) {
                    log.error("Error while creating query filter to hide past events using date field {}", dateField, filterException);
                }
            }
        }
        //filtering on 'Display on news hub and homepage' property, if it has been selected
        try {
            Filter filter = query.createFilter();
            filter.addEqualTo("../website:display", true);
            filters.add(filter);
        } catch (FilterException var7) {
            log.error("An exception occurred while trying to create a query filter showing document with display field on : {}", var7);
        }
    }

}
