package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.digital.common.components.info.SocialMediaComponentInfo;

import java.util.ArrayList;
import java.util.List;

@ParametersInfo(type = SocialMediaComponentInfo.class)
public class SocialMediaComponent extends CommonComponent {
    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        final SocialMediaComponentInfo componentParametersInfo = getComponentParametersInfo(request);

        List<HippoDocument> socialMediaItems = getSocialMediaItems(componentParametersInfo);
        request.setAttribute("socialmedia", socialMediaItems);

        String title = componentParametersInfo.getTitle();
        request.setAttribute("title", title);
    }

    public List<HippoDocument> getSocialMediaItems(SocialMediaComponentInfo componentInfo) {
        List<HippoDocument> beans = new ArrayList<>();
        this.addBeanForPath(componentInfo.getSocialMedia1(), beans);
        this.addBeanForPath(componentInfo.getSocialMedia2(), beans);
        this.addBeanForPath(componentInfo.getSocialMedia3(), beans);
        this.addBeanForPath(componentInfo.getSocialMedia4(), beans);
        this.addBeanForPath(componentInfo.getSocialMedia5(), beans);
        this.addBeanForPath(componentInfo.getSocialMedia6(), beans);
        this.addBeanForPath(componentInfo.getSocialMedia7(), beans);
        this.addBeanForPath(componentInfo.getSocialMedia8(), beans);
        this.addBeanForPath(componentInfo.getSocialMedia9(), beans);
        this.addBeanForPath(componentInfo.getSocialMedia10(), beans);
        return beans;
    }
}

