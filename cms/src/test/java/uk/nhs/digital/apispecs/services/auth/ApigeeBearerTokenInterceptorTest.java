package uk.nhs.digital.apispecs.services.auth;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

public class ApigeeBearerTokenInterceptorTest {

    private static final String TOKEN_URI = "https://login.apigee.com/oauth/token";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "pass";
    private static final String ACCESS_TOKEN = "abc123";

    @Mock private ApigeeOAuth2TokenProvider tokenProvider;
    @Mock private HttpRequest request;
    @Mock private ClientHttpRequestExecution execution;
    @Mock private ClientHttpResponse response;

    private HttpHeaders headers;
    private ApigeeBearerTokenInterceptor interceptor;

    @Before
    public void setUp() throws Exception {
        openMocks(this);

        headers = new HttpHeaders();
        interceptor = new ApigeeBearerTokenInterceptor(tokenProvider, TOKEN_URI, USERNAME, PASSWORD);

        given(tokenProvider.getAccessToken(TOKEN_URI, USERNAME, PASSWORD)).willReturn(ACCESS_TOKEN);
        given(request.getHeaders()).willReturn(headers);
        given(execution.execute(eq(request), any(byte[].class))).willReturn(response);
    }

    @Test
    public void addsBearerTokenToApiRequest_withoutSettingFormContentType() throws Exception {
        interceptor.intercept(request, new byte[0], execution);

        assertThat(headers.getFirst(HttpHeaders.AUTHORIZATION), is("Bearer " + ACCESS_TOKEN));
        assertThat(headers.getAccept(), contains(MediaType.APPLICATION_JSON));
        assertThat(headers.getContentType(), is(nullValue()));

        then(execution).should().execute(eq(request), any(byte[].class));
    }
}
