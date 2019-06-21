package uk.nhs.digital.nil.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.nil.beans.Indicator;

public class IndicatorComponent extends BaseHstComponent {

    private static final Logger log = LoggerFactory.getLogger(IndicatorComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        final HstRequestContext ctx = request.getRequestContext();
        Indicator indicator = getIndicator(ctx);

        request.setAttribute("indicator", indicator);
    }

    private Indicator getIndicator(HstRequestContext ctx) throws HstComponentException {
        HippoBean content = ctx.getContentBean();

        if (content instanceof Indicator) {
            return (Indicator) content;
        }

        log.warn("Cannot find Indicator document for: {}", content.getPath());
        throw new HstComponentException("Cannot find Indicator document based on request content");
    }
}
