package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.info.CardListComponentInfo;

import java.util.ArrayList;
import java.util.List;

@ParametersInfo(type = CardListComponentInfo.class)
public class CardListComponent extends CommonComponent {
    private static Logger LOGGER = LoggerFactory.getLogger(CardListComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        LOGGER.info("Card List Component Info");

        CardListComponentInfo info = getComponentParametersInfo(request);

        String headerText = info.getHeader();
        String introductionText = info.getIntroduction();

        request.setAttribute("headerText", headerText);
        request.setAttribute("introText", introductionText);

        String button1Title = info.getButton1Title();
        String button1Url = info.getButton1Link();
        String button2Title = info.getButton2Title();
        String button2Url = info.getButton2Link();
        String buttonIconLink = info.getButtonIconLink();
        String buttonIconLink2 = info.getButtonIconLink2();

        request.setAttribute("button1Title", button1Title);
        request.setAttribute("button1Url", button1Url);
        request.setAttribute("button2Title", button2Title);
        request.setAttribute("button2Url", button2Url);
        request.setAttribute("buttonIconLink", buttonIconLink);
        request.setAttribute("buttonIconLink2", buttonIconLink2);

        List<HippoDocument> cardComponents = getCardComponents(info);
        request.setAttribute("cardList", cardComponents);

        String template = info.getTemplate();
        request.setAttribute("template", template);
    }

    public List<HippoDocument> getCardComponents(CardListComponentInfo componentInfo) {
        List<HippoDocument> beans = new ArrayList<>();
        this.addBeanForPath(componentInfo.getCard1(), beans);
        this.addBeanForPath(componentInfo.getCard2(), beans);
        this.addBeanForPath(componentInfo.getCard3(), beans);
        this.addBeanForPath(componentInfo.getCard4(), beans);
        this.addBeanForPath(componentInfo.getCard5(), beans);
        this.addBeanForPath(componentInfo.getCard6(), beans);
        this.addBeanForPath(componentInfo.getCard7(), beans);
        this.addBeanForPath(componentInfo.getCard8(), beans);
        this.addBeanForPath(componentInfo.getCard9(), beans);
        this.addBeanForPath(componentInfo.getCard10(), beans);
        return beans;
    }
}
