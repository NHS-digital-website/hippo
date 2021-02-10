package uk.nhs.digital.intranet.components;

import org.hippoecm.hst.core.component.*;
import org.onehippo.cms7.essentials.components.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import uk.nhs.digital.intranet.model.IntranetUser;

public class HomePageComponent extends EssentialsContentComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        final SecurityContext context = SecurityContextHolder.getContext();
        final IntranetUser principal = (IntranetUser) context.getAuthentication().getPrincipal();
        request.setAttribute("username", principal.getFirstName());
    }
}
