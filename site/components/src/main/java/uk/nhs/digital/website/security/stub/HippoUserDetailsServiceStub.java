package uk.nhs.digital.website.security.stub;

import org.onehippo.forge.security.support.springsecurity.authentication.HippoUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import uk.nhs.digital.intranet.model.IntranetUser;

import java.util.Collections;

public class HippoUserDetailsServiceStub implements HippoUserDetailsService {

    private static final String FIRST_NAME = "Admin";
    private static final String LAST_NAME = "Admin";
    private static final String DUMMY_PASSWORD = "dummy-password";

    @Override
    public UserDetails loadUserByUsernameAndPassword(String username, String password) throws UsernameNotFoundException {
        return new IntranetUser(FIRST_NAME, LAST_NAME, username, password, Collections.emptyList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new IntranetUser(FIRST_NAME, LAST_NAME, username, DUMMY_PASSWORD, Collections.emptyList());
    }
}
