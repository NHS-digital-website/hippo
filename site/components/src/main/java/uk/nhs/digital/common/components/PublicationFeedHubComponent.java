package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.exceptions.FilterException;
import org.hippoecm.hst.content.beans.query.filter.BaseFilter;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.EssentialsListComponent;
import uk.nhs.digital.common.components.info.PublicationFeedHubComponentInfo;

import java.util.*;

@ParametersInfo(
    type = PublicationFeedHubComponentInfo.class
)
public class PublicationFeedHubComponent extends EssentialsListComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {

    }

    @Override
    protected Filter createQueryFilter(final HstRequest request, final HstQuery query) throws FilterException {
        return null;
    }

    @Override
    protected void contributeAndFilters(List<BaseFilter> filters, HstRequest request, HstQuery query) throws FilterException {
        super.contributeAndFilters(filters, request, query);

    }

}
