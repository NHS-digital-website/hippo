package uk.nhs.digital.intranet.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.EssentialsContentComponent;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import uk.nhs.digital.intranet.model.IntranetUser;

@ParametersInfo(type = IntranetSiteHeaderInfo.class)
public class IntranetSiteHeader extends EssentialsContentComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        // Should the search bar be enabled?
        IntranetSiteHeaderInfo parametersInfo = getComponentParametersInfo(request);
        request.setAttribute("enableSearch", parametersInfo.isEnableSearchEnabled());

        // Provide the username.
        final SecurityContext context = SecurityContextHolder.getContext();
        final IntranetUser principal = (IntranetUser) context.getAuthentication().getPrincipal();
        request.setAttribute("username", principal.getFirstName());
    }

}
