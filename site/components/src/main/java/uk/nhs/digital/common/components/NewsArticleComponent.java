package uk.nhs.digital.common.components;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.forge.selection.hst.contentbean.ValueList;
import org.onehippo.forge.selection.hst.util.SelectionUtil;


@ParametersInfo(
    type = NewsArticleComponent.class
)
public class NewsArticleComponent extends ContentRewriterComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        final ValueList creditbannerValueList = SelectionUtil.getValueListByIdentifier("creditbanner", RequestContextProvider.get());
        if (creditbannerValueList != null) {
            request.setAttribute("creditbanner", SelectionUtil.valueListAsMap(creditbannerValueList));
        }

        final ValueList creditbannerValueListLegacy = SelectionUtil.getValueListByIdentifier("creditbannerlegacy", RequestContextProvider.get());
        if (creditbannerValueListLegacy != null) {
            request.setAttribute("creditbannerlegacy", SelectionUtil.valueListAsMap(creditbannerValueListLegacy));
        }

        final ValueList newstypesValueList = SelectionUtil.getValueListByIdentifier("newstypes", RequestContextProvider.get());
        if (newstypesValueList != null) {
            request.setAttribute("newstypes", SelectionUtil.valueListAsMap(newstypesValueList));
        }

        request.setAttribute("currentUrl", request.getRequestURL().toString());
    }

}
