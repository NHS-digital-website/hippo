package uk.nhs.digital.intranet.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.springframework.util.StringUtils;
import uk.nhs.digital.intranet.model.Person;
import uk.nhs.digital.intranet.model.exception.ProviderCommunicationException;
import uk.nhs.digital.intranet.provider.GraphProvider;
import uk.nhs.digital.intranet.utils.Constants;

public class PersonComponent extends BaseHstComponent {

    private static final String PROVIDER_COMMUNICATION_ERROR_MESSAGE = "Unable to perform operation.";

    private final GraphProvider graphProvider;

    public PersonComponent(GraphProvider graphProvider) {
        this.graphProvider = graphProvider;
    }

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        final String accessToken = (String) RequestContextProvider.get().getAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME);
        if (!StringUtils.hasText(accessToken)) {
            request.setAttribute("accessTokenRequired", true);
            return;
        }

        final String id = getComponentParameter("id");
        try {
            final Person person = graphProvider.getPerson(id);
            request.setAttribute("person", person);
        } catch (final ProviderCommunicationException e) {
            request.setAttribute("errorMessage", PROVIDER_COMMUNICATION_ERROR_MESSAGE);
        }
    }
}
