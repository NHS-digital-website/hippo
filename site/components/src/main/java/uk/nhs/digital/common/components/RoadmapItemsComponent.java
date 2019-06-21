package uk.nhs.digital.common.components;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.forge.selection.hst.contentbean.ValueList;
import org.onehippo.forge.selection.hst.util.SelectionUtil;

public class RoadmapItemsComponent extends BaseGaContentComponent {

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

    }

}
