package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.EssentialsListComponent;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import uk.nhs.digital.common.components.info.ListPickerComponentInfo;

import java.util.ArrayList;
import java.util.List;

@ParametersInfo(type = ListPickerComponentInfo.class)
public class ListPickerComponent extends EssentialsListComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {

        final ListPickerComponentInfo paramInfo = getComponentParametersInfo(request);
        final List<HippoDocument> documentItems = getDocumentItems(paramInfo);
        final int pageSize = getPageSize(request, paramInfo);
        final int page = getAnyIntParameter(request, REQUEST_PARAM_PAGE, 1);
        final Pageable<HippoDocument> pageable = getPageableFactory().createPageable(documentItems, page, pageSize);
        request.setModel(REQUEST_ATTR_PAGEABLE, pageable);
        request.setAttribute(REQUEST_ATTR_PARAM_INFO, paramInfo);
    }

    private List<HippoDocument> getDocumentItems(final ListPickerComponentInfo componentInfo) {
        final List<HippoDocument> beans = new ArrayList<>();
        addBeanForPath(componentInfo.getDocumentItem1(), beans);
        addBeanForPath(componentInfo.getDocumentItem2(), beans);
        addBeanForPath(componentInfo.getDocumentItem3(), beans);
        addBeanForPath(componentInfo.getDocumentItem4(), beans);
        addBeanForPath(componentInfo.getDocumentItem5(), beans);
        addBeanForPath(componentInfo.getDocumentItem6(), beans);
        addBeanForPath(componentInfo.getDocumentItem7(), beans);
        addBeanForPath(componentInfo.getDocumentItem8(), beans);
        addBeanForPath(componentInfo.getDocumentItem9(), beans);
        addBeanForPath(componentInfo.getDocumentItem10(), beans);
        return beans;
    }
}
