package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.digital.common.components.info.CaseStudyComponentInfo;

@ParametersInfo(type = CaseStudyComponentInfo.class)
public class CaseStudyComponent extends CommonComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        CaseStudyComponentInfo componentInfo = getComponentParametersInfo(request);
        final String textAlignment = componentInfo.getTextAlignment();
        HippoBean document = getHippoBeanForPath(componentInfo.getDocument(), HippoBean.class);

        request.setAttribute("document", document);
        request.setAttribute("textAlignment", textAlignment);
    }
}
