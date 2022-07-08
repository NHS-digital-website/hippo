package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.CommonComponent;

public class MacroTestComponent extends CommonComponent {
    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        String macro = getPublicRequestParameter(request, "macro");
        request.setAttribute("macro", macro);

        try {
            setBeans(request);
        } catch (ObjectBeanManagerException e) {
            e.printStackTrace();
        }
    }

    private void setBeans(HstRequest request) throws ObjectBeanManagerException {
        HippoBean imageBean = (HippoBean) request.getRequestContext().getObjectBeanManager().getObject("/content/gallery/test-resources/fibre.jpg");
        request.setAttribute("imageBean", imageBean);
    }
}
