package uk.nhs.digital.common.components;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.exceptions.FilterException;
import org.hippoecm.hst.content.beans.query.filter.BaseFilter;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.EssentialsBlogComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsPageable;
import uk.nhs.digital.common.components.info.LatestBlogComponentInfo;

import java.util.*;

@ParametersInfo(
    type = LatestBlogComponentInfo.class
)
public class LatestBlogComponent extends EssentialsBlogComponent {

    @Override
    protected void contributeAndFilters(final List<BaseFilter> filters, final HstRequest request, final HstQuery query) throws FilterException {
        super.contributeAndFilters(filters, request, query);
        query.addOrderByDescending("website:dateofpublication");
    }

    @Override
    protected int getPageSize(final HstRequest request, final EssentialsPageable paramInfo) {
        //getting the componentPageSize parameter value if defined in the component
        String compononentPageSize = StringUtils.defaultIfEmpty(getComponentParameter("defaultPageSize"), "");
        //if the componentPageSize hasn't been defined, then use the component param info
        return NumberUtils.isCreatable(compononentPageSize)
            ? Integer.parseInt(compononentPageSize) : super.getPageSize(request, paramInfo);
    }
}
