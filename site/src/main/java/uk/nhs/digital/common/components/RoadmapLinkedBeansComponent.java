package uk.nhs.digital.common.components;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.forge.selection.hst.contentbean.ValueList;
import org.onehippo.forge.selection.hst.contentbean.ValueListItem;
import org.onehippo.forge.selection.hst.util.SelectionUtil;


import java.util.List;

public class RoadmapLinkedBeansComponent extends BaseGaContentComponent {

    //private static Logger log = LoggerFactory.getLogger(PublishedWorkLinkedBeansComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        //sending the roadmapcategories values
        final ValueList roadmapCategoriesValueList =
            SelectionUtil.getValueListByIdentifier("roadmapcategories", RequestContextProvider.get());
        if (roadmapCategoriesValueList != null) {
            request.setAttribute("roadmapcategories", SelectionUtil.valueListAsMap(roadmapCategoriesValueList));
            List<ValueListItem> valueListItemList = roadmapCategoriesValueList.getItems();
            for (ValueListItem valListItem : valueListItemList) {
                System.out.println(valListItem.getKey());
                System.out.println(valListItem.getLabel());
            }
        }

        //sending the effective date status values
        final ValueList effectiveDateStatusValueList =
            SelectionUtil.getValueListByIdentifier("effectivedatestatus", RequestContextProvider.get());
        if (effectiveDateStatusValueList != null) {
            request.setAttribute("effectivedatestatus", SelectionUtil.valueListAsMap(effectiveDateStatusValueList));
            List<ValueListItem> valueListItemList = effectiveDateStatusValueList.getItems();
            for (ValueListItem valListItem : valueListItemList) {
                System.out.println(valListItem.getKey());
                System.out.println(valListItem.getLabel());
            }
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
