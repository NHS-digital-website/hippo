package uk.nhs.digital.common.components;

import org.hippoecm.hst.container.*;
import org.hippoecm.hst.core.component.*;
import org.onehippo.forge.selection.hst.contentbean.*;
import org.onehippo.forge.selection.hst.util.*;

public class NewsComponent extends BaseGaContentComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        final ValueList newsTypeValueList =
            SelectionUtil.getValueListByIdentifier("news-type", RequestContextProvider.get());
        if (newsTypeValueList != null) {
            request.setAttribute("newsTypeMap", SelectionUtil.valueListAsMap(newsTypeValueList));
        }
    }
}
