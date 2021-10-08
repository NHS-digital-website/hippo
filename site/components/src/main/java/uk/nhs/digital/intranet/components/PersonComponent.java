package uk.nhs.digital.intranet.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.springframework.util.StringUtils;
import uk.nhs.digital.intranet.factory.PersonFactory;
import uk.nhs.digital.intranet.model.Person;
import uk.nhs.digital.intranet.model.exception.ProviderCommunicationException;
import uk.nhs.digital.intranet.utils.Constants;

public class PersonComponent extends BaseHstComponent {

    private static final String PROVIDER_COMMUNICATION_ERROR_MESSAGE = "Unable to perform operation.";

    private final PersonFactory personFactory;

    public PersonComponent(PersonFactory personFactory) {
        this.personFactory = personFactory;
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
            final Person person = personFactory.fetchPerson(id);
            request.setAttribute("person", person);
        } catch (final ProviderCommunicationException e) {
            request.setAttribute("errorMessage", PROVIDER_COMMUNICATION_ERROR_MESSAGE);
        }
    }
}
