package uk.nhs.digital.website.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import uk.nhs.digital.intranet.model.IntranetUser;

import java.util.Collection;
import java.util.Collections;

public class SamlUserDetailsService implements SAMLUserDetailsService {

    private static final String DUMMY_PASSWORD = "dummy_password";
    private static final String ADFS_ATTR_GIVEN_NAME = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/givenname";
    private static final String ADFS_ATTR_SURNAME = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/surname";

    @Override
    public Object loadUserBySAML(SAMLCredential samlCredential) throws UsernameNotFoundException {
        final String shortCode = samlCredential.getNameID().getValue();
        final String firstName = samlCredential.getAttributeAsString(ADFS_ATTR_GIVEN_NAME);
        final String lastName = samlCredential.getAttributeAsString(ADFS_ATTR_SURNAME);
        final Collection<GrantedAuthority> authorities = Collections.emptyList();

        return new IntranetUser(firstName, lastName, shortCode, DUMMY_PASSWORD, authorities);
    }
}
