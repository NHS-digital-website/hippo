package uk.nhs.digital.filter;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticateFilter extends AbstractAuthenticationProcessingFilter {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthorizedClientRepository authorizedClientRepository;
    private final String filterProcessesUrl;

    public AuthenticateFilter(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository, String url) {
        super(url);
        this.filterProcessesUrl = url;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizedClientRepository = authorizedClientRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        OAuth2LoginAuthenticationFilter filter = new OAuth2LoginAuthenticationFilter(clientRegistrationRepository, authorizedClientRepository, filterProcessesUrl);
        filter.setAuthenticationManager(super.getAuthenticationManager());
        return filter.attemptAuthentication(request, response);
    }

    @Override
    public boolean requiresAuthentication(HttpServletRequest request,
                                          HttpServletResponse response) {
        MultiValueMap<String, String> params = toMultiMap(request.getParameterMap());
        return isAuthorizationResponseSuccess(params);
    }

    static boolean isAuthorizationResponseSuccess(MultiValueMap<String, String> request) {
        return StringUtils.hasText(request.getFirst(OAuth2ParameterNames.CODE))
            && StringUtils.hasText(request.getFirst(OAuth2ParameterNames.STATE));
    }

    private MultiValueMap<String, String> toMultiMap(Map<String, String[]> map) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>(map.size());
        map.forEach((key, values) -> {
            if (values.length > 0) {
                for (String value : values) {
                    params.add(key, value);
                }
            }
        });
        return params;
    }

}
