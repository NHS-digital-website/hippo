package uk.nhs.digital.intranet.components;

import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.EssentialsNewsComponent;
import org.onehippo.cms7.essentials.components.paging.IterablePagination;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import uk.nhs.digital.intranet.components.info.IntraNewsComponentInfo;

import java.util.ArrayList;
import java.util.List;


@ParametersInfo(
    type = IntraNewsComponentInfo.class
)
public class IntraNewsComponent extends EssentialsNewsComponent {


    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        final IntraNewsComponentInfo paramInfo = getComponentParametersInfo(request);

        String featuredPath = paramInfo.getFeaturedArticle();
        setContentBeanForPath(featuredPath, request, response);
        List<HippoDocument> taskDocuments = getTaskDocuments(paramInfo);
        Pageable<HippoDocument> pageable = new IterablePagination<>(taskDocuments, 1, 8);

        request.setAttribute("taskDocuments", pageable);
        request.setAttribute(REQUEST_ATTR_PARAM_INFO, paramInfo);
        super.doBeforeRender(request, response);
    }

    private List<HippoDocument> getTaskDocuments(final IntraNewsComponentInfo componentInfo) {
        final List<HippoDocument> beans = new ArrayList<>();
        addBeanForPath(componentInfo.getTaskOne(), beans);
        addBeanForPath(componentInfo.getTaskTwo(), beans);
        addBeanForPath(componentInfo.getTaskThree(), beans);
        addBeanForPath(componentInfo.getTaskFour(), beans);
        addBeanForPath(componentInfo.getTaskFive(), beans);
        addBeanForPath(componentInfo.getTaskSix(), beans);
        addBeanForPath(componentInfo.getTaskSeven(), beans);
        addBeanForPath(componentInfo.getTaskEight(), beans);
        return beans;
    }
}
