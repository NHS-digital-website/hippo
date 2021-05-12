package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.info.CardListComponentInfo;

@ParametersInfo(type = CardListComponentInfo.class)
public class CardListComponent extends CommonComponent {
    private static Logger LOGGER = LoggerFactory.getLogger(CardListComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        LOGGER.info("Card List Component Info");
    }
}
