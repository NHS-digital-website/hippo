package uk.nhs.digital.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component

public class OauthClientRegistration {

    private static final Logger LOGGER = LoggerFactory.getLogger(OauthClientRegistration.class);

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(keyCloakClientRegistration());
    }

    private ClientRegistration keyCloakClientRegistration() {
        return ClientRegistration.withRegistrationId("keycloak")
            .clientId("7d358d7e-e350-482e-ab43-d4c658a710bf")
            .clientSecret("rbeBX3Uo2yRcc2WgWYPJJ0NcYVx3f2CvJVJbkjXk")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .scope("openid", "email", "profile")
            .authorizationUri("https://fs.nhs.net/adfs/oauth2/authorize/")
            .tokenUri("https://fs.nhs.net/adfs/oauth2/token/")
            .userInfoUri("https://fs.nhs.net/adfs/userinfo")
            .userNameAttributeName(IdTokenClaimNames.SUB)
            .jwkSetUri("https://fs.nhs.net/adfs/discovery/keys")
            .clientName("KeyCloak")
            .redirectUri("https://dev.nhsd.io/intranet")
            .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
            .userInfoAuthenticationMethod(AuthenticationMethod.HEADER)
            .build();
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        final OidcUserService delegate = new OidcUserService();
        return (userRequest) -> {
            // Delegate to the default implementation for loading a user
            OidcUser oidcUser = delegate.loadUser(userRequest);

            OAuth2AccessToken accessToken = userRequest.getAccessToken();
            LOGGER.error("Value of Access Token is " + accessToken.getTokenValue());
            LOGGER.error(" *****************************************************************");
            LOGGER.error("Value of Id Token is " + userRequest.getIdToken().getTokenValue());
            LOGGER.error(" *****************************************************************");
            LOGGER.error("Value of oidcUser is " + oidcUser);
            LOGGER.error(" *****************************************************************");
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            // TODO
            // 1) Fetch the authority information from the protected resource using accessToken
            // 2) Map the authority information to one or more GrantedAuthority's and add it to mappedAuthorities

            // 3) Create a copy of oidcUser but use the mappedAuthorities instead
            oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
            LOGGER.error("Value of oidc user getUserInfo " + oidcUser.getUserInfo());
            LOGGER.error("Value of oidc user getFamilyName " + oidcUser.getFamilyName());
            LOGGER.error("Value of oidc user getFullName " + oidcUser.getFullName());
            LOGGER.error("Value of oidc user getEmail " + oidcUser.getEmail());
            for (String key : oidcUser.getAttributes().keySet()) {
                LOGGER.error("Value of key --> " + key + "  Value -->" + oidcUser.getAttribute(key));
            }
            LOGGER.error("Value of oidc user getEmail " + oidcUser.getAttributes());
            return oidcUser;
        };
    }
}
