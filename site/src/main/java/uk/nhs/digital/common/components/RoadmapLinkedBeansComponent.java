package uk.nhs.digital.common.components;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.exceptions.FilterException;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.onehippo.forge.selection.hst.contentbean.ValueList;
import org.onehippo.forge.selection.hst.util.SelectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.website.beans.Roadmap;
import uk.nhs.digital.website.beans.RoadmapItem;

import java.util.Arrays;

public class RoadmapLinkedBeansComponent extends BaseGaContentComponent {

    private static Logger log =
        LoggerFactory.getLogger(PublishedWorkLinkedBeansComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        //sending the roadmapcategories values
        final ValueList roadmapCategoriesValueList =
            SelectionUtil.getValueListByIdentifier("roadmapcategories", RequestContextProvider.get());
        if (roadmapCategoriesValueList != null) {
            request.setAttribute("roadmapcategories", SelectionUtil.valueListAsMap(roadmapCategoriesValueList));
        }

        //sending the effective date status values
        final ValueList effectiveDateStatusValueList =
            SelectionUtil.getValueListByIdentifier("effectivedatestatus", RequestContextProvider.get());
        if (effectiveDateStatusValueList != null) {
            request.setAttribute("effectivedatestatus", SelectionUtil.valueListAsMap(effectiveDateStatusValueList));
        }

        //linkPath property contains the relative path of the links property in the Roadmap Item document
        String linkPath = getComponentParameter("linkPath");

        final HstRequestContext context = request.getRequestContext();
        // we assume a Roadmap as content bean, thus expect a RoadmapItem as "child" document
        Roadmap roadmap = context.getContentBean(Roadmap.class);
        try {
            HstQuery linkedBeanQuery = ContentBeanUtils.createIncomingBeansQuery(
                roadmap, context.getSiteContentBaseBean(),
                linkPath,
                RoadmapItem.class, false);

            //fetch the selected category from the request
            String[] selectedTypes = getSelectedTypes(request);
            request.setAttribute("selectedTypes", Arrays.asList(selectedTypes));
            if (selectedTypes.length > 0) {
                final Filter filter = linkedBeanQuery.createFilter();
                for (String type : selectedTypes) {
                    try {
                        filter.addEqualTo("@website:markers", type);
                    } catch (FilterException filterException) {
                        log.warn("Errors while adding event type filter {}", filterException);
                    }
                }
                linkedBeanQuery.setFilter(filter);
            }

            //linked documents will contain the child Roadmap item documents
            HstQueryResult roadmapitems = linkedBeanQuery.execute();
            request.setAttribute("roadmapitems", roadmapitems);
        } catch (QueryException queryException) {
            log.warn(
                "QueryException ", queryException);
        }
    }

    /**
     * Fetch the values of type parameters from the URL query string
     *
     * @param request containing the type parameters
     * @return array of type parameters if at least one exists, otherwise empty
     */
    protected String[] getSelectedTypes(HstRequest request) {
        return getPublicRequestParameters(request, "type");
    }



}
