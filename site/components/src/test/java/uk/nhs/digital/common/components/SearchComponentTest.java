package uk.nhs.digital.common.components;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.openMocks;

import com.onehippo.search.integration.api.ExternalSearchService;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.hippoecm.hst.configuration.channel.ChannelInfo;
import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.configuration.hosting.PortMount;
import org.hippoecm.hst.configuration.hosting.VirtualHost;
import org.hippoecm.hst.configuration.hosting.VirtualHosts;
import org.hippoecm.hst.configuration.site.HstSite;
import org.hippoecm.hst.container.ModifiableRequestContextProvider;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.core.request.HstSiteMapMatcher;
import org.hippoecm.hst.core.request.ResolvedMount;
import org.hippoecm.hst.core.request.ResolvedVirtualHost;
import org.hippoecm.hst.mock.core.component.MockHstRequest;
import org.hippoecm.hst.mock.core.request.MockHstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.hippoecm.hst.site.request.ResolvedMountImpl;
import org.hippoecm.hst.site.request.ResolvedVirtualHostImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.onehippo.cms7.services.hst.Channel;
import uk.nhs.digital.common.ServiceProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

@RunWith(DataProviderRunner.class)
public class SearchComponentTest {
    private String randomString = makeRandomString();
    private SearchComponent searchComponent;
    private MockHstRequestContext requestContext;
    private MockHstRequest request;
    private ResolvedMount resolvedMount;

    private VirtualHost virtualHost = new VirtualHost() {
        @Override
        public String getHostName() {
            return "localhost";
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getHostGroupName() {
            return null;
        }

        @Override
        public String getLocale() {
            return null;
        }

        @Override
        public VirtualHost getChildHost(String name) {
            return null;
        }

        @Override
        public List<VirtualHost> getChildHosts() {
            return null;
        }

        @Override
        public PortMount getPortMount(int portNumber) {
            return null;
        }

        @Override
        public VirtualHosts getVirtualHosts() {
            return null;
        }

        @Override
        public boolean isContextPathInUrl() {
            return false;
        }

        @Override
        public String getContextPath() {
            return null;
        }

        @Override
        public boolean isPortInUrl() {
            return false;
        }

        @Override
        public String getScheme() {
            return null;
        }

        @Override
        public boolean isSchemeAgnostic() {
            return false;
        }

        @Override
        public int getSchemeNotMatchingResponseCode() {
            return 0;
        }

        @Override
        public String getHomePage() {
            return null;
        }

        @Override
        public String getBaseURL(HttpServletRequest request) {
            return null;
        }

        @Override
        public String getPageNotFound() {
            return null;
        }

        @Override
        public boolean isVersionInPreviewHeader() {
            return false;
        }

        @Override
        public boolean isCacheable() {
            return false;
        }

        @Override
        public String[] getDefaultResourceBundleIds() {
            return new String[0];
        }

        @Override
        public String getCdnHost() {
            return null;
        }

        @Override
        public String getHstLinkUrlPrefix() {
            return null;
        }

        @Override
        public boolean isCustomHttpsSupported() {
            return false;
        }

        @Override
        public Map<String, String> getResponseHeaders() {
            return null;
        }

        @Override
        public Collection<String> getAllowedOrigins() {
            return null;
        }
    };

    private Mount mount = new Mount() {
        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getAlias() {
            return null;
        }

        @Override
        public boolean isMapped() {
            return false;
        }

        @Override
        public Mount getParent() {
            return null;
        }

        @Override
        public String getMountPoint() {
            return null;
        }

        @Override
        public boolean hasNoChannelInfo() {
            return false;
        }

        @Override
        public String getContentPath() {
            return null;
        }

        @Override
        public String getMountPath() {
            return null;
        }

        @Override
        public List<Mount> getChildMounts() {
            return null;
        }

        @Override
        public Mount getChildMount(String name) {
            return null;
        }

        @Override
        public VirtualHost getVirtualHost() {
            return virtualHost;
        }

        @Override
        public HstSite getHstSite() {
            return null;
        }

        @Override
        public boolean isContextPathInUrl() {
            return false;
        }

        @Override
        public boolean isPortInUrl() {
            return false;
        }

        @Override
        public int getPort() {
            return 0;
        }

        @Override
        public String getContextPath() {
            return null;
        }

        @Override
        public String getHomePage() {
            return null;
        }

        @Override
        public String getPageNotFound() {
            return null;
        }

        @Override
        public String getScheme() {
            return null;
        }

        @Override
        public String getHstLinkUrlPrefix() {
            return null;
        }

        @Override
        public boolean isSchemeAgnostic() {
            return false;
        }

        @Override
        public boolean containsMultipleSchemes() {
            return false;
        }

        @Override
        public int getSchemeNotMatchingResponseCode() {
            return 0;
        }

        @Override
        public boolean isPreview() {
            return false;
        }

        @Override
        public boolean isOfType(String type) {
            return false;
        }

        @Override
        public String getType() {
            return null;
        }

        @Override
        public List<String> getTypes() {
            return null;
        }

        @Override
        public boolean isVersionInPreviewHeader() {
            return false;
        }

        @Override
        public String getNamedPipeline() {
            return null;
        }

        @Override
        public boolean isFinalPipeline() {
            return false;
        }

        @Override
        public String getLocale() {
            return null;
        }

        @Override
        public HstSiteMapMatcher getHstSiteMapMatcher() {
            return null;
        }

        @Override
        public boolean isAuthenticated() {
            return false;
        }

        @Override
        public Set<String> getRoles() {
            return null;
        }

        @Override
        public Set<String> getUsers() {
            return null;
        }

        @Override
        public boolean isSubjectBasedSession() {
            return false;
        }

        @Override
        public boolean isSessionStateful() {
            return false;
        }

        @Override
        public String getFormLoginPage() {
            return null;
        }

        @Override
        public String getProperty(String name) {
            return null;
        }

        @Override
        public List<String> getPropertyNames() {
            return null;
        }

        @Override
        public Map<String, String> getMountProperties() {
            return null;
        }

        @Override
        public String getParameter(String name) {
            return null;
        }

        @Override
        public Map<String, String> getParameters() {
            return null;
        }

        @Override
        public String getIdentifier() {
            return null;
        }

        @Override
        public <T extends ChannelInfo> T getChannelInfo() {
            return null;
        }

        @Override
        public Channel getChannel() {
            return null;
        }

        @Override
        public String[] getDefaultSiteMapItemHandlerIds() {
            return new String[0];
        }

        @Override
        public boolean isCacheable() {
            return false;
        }

        @Override
        public String[] getDefaultResourceBundleIds() {
            return new String[0];
        }

        @Override
        public Map<String, String> getResponseHeaders() {
            return null;
        }

        @Override
        public boolean isExplicit() {
            return false;
        }

        @Override
        public String getPageModelApi() {
            return null;
        }
    };

    private HttpServletRequest servletRequest = new HttpServletRequest() {
        @Override
        public String getAuthType() {
            return null;
        }

        @Override
        public Cookie[] getCookies() {
            return new Cookie[0];
        }

        @Override
        public long getDateHeader(String name) {
            return 0;
        }

        @Override
        public String getHeader(String name) {
            return null;
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            return null;
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            return null;
        }

        @Override
        public int getIntHeader(String name) {
            return 0;
        }

        @Override
        public String getMethod() {
            return null;
        }

        @Override
        public String getPathInfo() {
            return null;
        }

        @Override
        public String getPathTranslated() {
            return null;
        }

        @Override
        public String getContextPath() {
            return null;
        }

        @Override
        public String getQueryString() {
            return null;
        }

        @Override
        public String getRemoteUser() {
            return null;
        }

        @Override
        public boolean isUserInRole(String role) {
            return false;
        }

        @Override
        public Principal getUserPrincipal() {
            return null;
        }

        @Override
        public String getRequestedSessionId() {
            return null;
        }

        @Override
        public String getRequestURI() {
            return null;
        }

        @Override
        public StringBuffer getRequestURL() {
            return new StringBuffer("https://localhost/" + randomString);
        }

        @Override
        public String getServletPath() {
            return null;
        }

        @Override
        public HttpSession getSession(boolean create) {
            return null;
        }

        @Override
        public HttpSession getSession() {
            return null;
        }

        @Override
        public String changeSessionId() {
            return null;
        }

        @Override
        public boolean isRequestedSessionIdValid() {
            return false;
        }

        @Override
        public boolean isRequestedSessionIdFromCookie() {
            return false;
        }

        @Override
        public boolean isRequestedSessionIdFromURL() {
            return false;
        }

        @Override
        public boolean isRequestedSessionIdFromUrl() {
            return false;
        }

        @Override
        public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
            return false;
        }

        @Override
        public void login(String username, String password) throws ServletException {

        }

        @Override
        public void logout() throws ServletException {

        }

        @Override
        public Collection<Part> getParts() throws IOException, ServletException {
            return null;
        }

        @Override
        public Part getPart(String name) throws IOException, ServletException {
            return null;
        }

        @Override
        public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
            return null;
        }

        @Override
        public Object getAttribute(String name) {
            return null;
        }

        @Override
        public Enumeration<String> getAttributeNames() {
            return null;
        }

        @Override
        public String getCharacterEncoding() {
            return null;
        }

        @Override
        public void setCharacterEncoding(String env) throws UnsupportedEncodingException {

        }

        @Override
        public int getContentLength() {
            return 0;
        }

        @Override
        public long getContentLengthLong() {
            return 0;
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return null;
        }

        @Override
        public String getParameter(String name) {
            return null;
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return null;
        }

        @Override
        public String[] getParameterValues(String name) {
            return new String[0];
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return null;
        }

        @Override
        public String getProtocol() {
            return null;
        }

        @Override
        public String getScheme() {
            return null;
        }

        @Override
        public String getServerName() {
            return null;
        }

        @Override
        public int getServerPort() {
            return 0;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return null;
        }

        @Override
        public String getRemoteAddr() {
            return null;
        }

        @Override
        public String getRemoteHost() {
            return null;
        }

        @Override
        public void setAttribute(String name, Object o) {

        }

        @Override
        public void removeAttribute(String name) {

        }

        @Override
        public Locale getLocale() {
            return null;
        }

        @Override
        public Enumeration<Locale> getLocales() {
            return null;
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public RequestDispatcher getRequestDispatcher(String path) {
            return null;
        }

        @Override
        public String getRealPath(String path) {
            return null;
        }

        @Override
        public int getRemotePort() {
            return 0;
        }

        @Override
        public String getLocalName() {
            return null;
        }

        @Override
        public String getLocalAddr() {
            return null;
        }

        @Override
        public int getLocalPort() {
            return 0;
        }

        @Override
        public ServletContext getServletContext() {
            return null;
        }

        @Override
        public AsyncContext startAsync() throws IllegalStateException {
            return null;
        }

        @Override
        public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
            return null;
        }

        @Override
        public boolean isAsyncStarted() {
            return false;
        }

        @Override
        public boolean isAsyncSupported() {
            return false;
        }

        @Override
        public AsyncContext getAsyncContext() {
            return null;
        }

        @Override
        public DispatcherType getDispatcherType() {
            return null;
        }
    };

    @Mock private ComponentManager compManager;
    @Mock private HstResponse response;
    @Mock private ExternalSearchService searchService;
    @Mock private ServiceProvider serviceProvider;
    @Mock private ResolvedVirtualHost resolvedVirtualHost;
    @Mock private PortMount portMount;

    private String makeRandomString() {
        String usethese = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 7) { // length of the random string.
            int index = (int) (rnd.nextFloat() * usethese.length());
            salt.append(usethese.charAt(index));
        }

        return salt.toString();
    }

    @Before
    public void setUp() throws Exception {
        openMocks(this);

        HstServices.setComponentManager(compManager);

        requestContext = new MockHstRequestContext();
        request = new MockHstRequest();
        resolvedVirtualHost = new ResolvedVirtualHostImpl(virtualHost, "hostname", portMount);
        resolvedMount = new ResolvedMountImpl(mount, resolvedVirtualHost, "/test", "", 80);

        ModifiableRequestContextProvider.set(requestContext);

        searchComponent = new SearchComponent();
    }

    @Test
    @UseDataProvider("validInputsAndExpectedOutputs")
    public void formatValidSearchInputsToIncludeWildcardCharacters(final String queryInput, final String expectedQueryOutput)
        throws Exception {

        // given
        // setUp

        // when
        final String actualQueryOutput = ComponentUtils.parseAndApplyWildcards(queryInput);

        // then
        assertEquals("Wildcard query is correct", expectedQueryOutput, actualQueryOutput);

    }

    @Test
    @UseDataProvider("pageableTests")
    public void pageableNumbersTest(long maxPages, int currentPage, List<Integer> expected) throws Exception {

        Pageable pageable = mock(Pageable.class);
        given(pageable.getTotalPages()).willReturn(maxPages);
        given(pageable.getCurrentPage()).willReturn(currentPage);

        // then
        assertEquals("Pageable numbers are as expected", expected, searchComponent.getHstPageNumbers(pageable));
    }

    @Test
    @UseDataProvider("facetFieldData")
    public void testConfigureFacets(final String key, final Object taxonomyData) {
        //given
        given(serviceProvider.getService(ExternalSearchService.class)).willReturn(searchService);

        //setUp
        request.addParameter("topic", "param 1");
        requestContext.setServletRequest(servletRequest);
        requestContext.setResolvedMount(resolvedMount);
        request.setRequestContext(requestContext);

        Map<String, Object> facetFields = new LinkedHashMap<>();
        facetFields.put(key, taxonomyData);

        assertThat("Current URL ", (String) request.getAttribute("currentUrl"), is(nullValue()));

        //when
        searchComponent.configureFacets(facetFields, request, 10);

        //then
        assertThat("Current URL ", (String) request.getAttribute("currentUrl"), containsString(randomString));
        assertThat("Taxonomy list", ((ArrayList) facetFields.get(key)).size(), is(3));
        assertThat("Taxonomy node 1",
            ((LinkedHashMap) ((ArrayList) facetFields.get(key)).get(0)).get("facetUrl").toString(),
            is(servletRequest.getRequestURL().toString() + "?topic=0"));
        assertThat("Taxonomy node 2",
            ((LinkedHashMap) ((ArrayList) facetFields.get(key)).get(1)).get("facetUrl").toString(),
            is(servletRequest.getRequestURL().toString() + "?topic=null"));
        assertThat("Taxonomy node 3",
            ((LinkedHashMap) ((ArrayList) facetFields.get(key)).get(2)).get("facetUrl").toString(),
            is(servletRequest.getRequestURL().toString() + "?topic=root"));
    }

    @DataProvider
    public static Object[][] facetFieldData() {
        final Map<String, Object> mo = new LinkedHashMap<>();
        mo.put("name", "0");
        mo.put("name", "1");
        mo.put("name", "0");

        final Map<String, Object> mo2 = new LinkedHashMap<>();
        mo.put("name", "0");
        mo.put("name", "0");
        mo.put("name", "0");

        ArrayList<Map<String, Object>> taxData = new ArrayList<>();
        taxData.add(mo);
        taxData.add(mo2);

        return new Object[][]{
            {"taxonomyClassificationField_taxonomyAllValues_keyPath", taxData}
        };
    }

    @DataProvider
    public static Object[][] validInputsAndExpectedOutputs() {

        return new Object[][] {

            // queryInput                   expectedQueryOutput
            {"lorem",                       "lorem*"},
            {"lorem*",                      "lorem*"},
            {"lorem*?",                     "lorem*"},
            {"lorem*? ipsum",               "lorem* ipsum*"},
            {"lorem ipsum",                 "lorem* ipsum*"},
            {"lorem ipsum*",                "lorem* ipsum*"},
            {"lor* ipsum",                  "lor* ipsum*"},
            {"lorem ips*",                  "lorem* ips*"},
            {"lo ip",                       "lo ip"},
            {"lor ip",                      "lor* ip"},

            {"\"dolor sit\"",               "\"dolor sit\""},
            {"\"dolor sit\" ipsum",         "\"dolor sit\" ipsum*"},
            {"\"dolor sit\" ipsum*",        "\"dolor sit\" ipsum*"},
            {"lorem \"dolor sit\" ipsum",   "lorem* \"dolor sit\" ipsum*"},
            {"lorem ipsum \"dolor sit\"",   "lorem* ipsum* \"dolor sit\""},

            {"-lorem",                      "-lorem*"},
            {"lorem -ipsum",                "lorem* -ipsum*"},
            {"lor* -ipsum",                 "lor* -ipsum*"},
            {"\"dolor sit\" -\"lorem ipsum\"","\"dolor sit\" \"lorem ipsum\""},

            {"lorem OR ipsum",              "lorem* OR ipsum*"},
            {"lorem OR ipsum sit",          "lorem* OR ipsum* sit*"},
            {"OR lorem",                    "lorem*"},
            {"lorem OR ipsum \"dolor sit\"","lorem* OR ipsum* \"dolor sit\""},
            {"\"dolor sit\" lorem OR ipsum","\"dolor sit\" lorem* OR ipsum*"},

            {"elit.",                       "elit."},
            {"ante,",                       "ante,"},
            {"elit. lorem",                 "elit. lorem*"},
            {"elit. -lorem",                "elit. -lorem*"},
            {"-elit. lorem",                "-elit. lorem*"},
            {"elit. \"dolor sit\"",         "elit. \"dolor sit\""},

            // Check that odd numbers of quotes are sanitized
            {"elit \"dolor sit",         "elit* dolor* sit*"},
            {"elit \"dolor\" sit\"",     "elit* \"dolor\" sit*"}

        };
    }

    @DataProvider
    public static Object[][] pageableTests() {
        // max pages
        // current page
        // expected range

        return new Object[][] {
            new Object[]{
                2,
                1,
                asList(1, 2)
            },
            new Object[]{
                2,
                2,
                asList(1, 2)
            },
            new Object[]{
                5,
                1,
                asList(1, 2, 3, 4, 5)
            },
            new Object[]{
                5,
                5,
                asList(1, 2, 3, 4, 5)
            },
            new Object[]{
                6,
                1,
                asList(1, 2, 3, 4, 5)
            },
            new Object[]{
                6,
                3,
                asList(1, 2, 3, 4, 5)
            },
            new Object[]{
                6,
                4,
                asList(2, 3, 4, 5, 6)
            },
            new Object[]{
                6,
                6,
                asList(2, 3, 4, 5, 6)
            },
            new Object[]{
                57,
                23,
                asList(21, 22, 23, 24, 25)
            },
        };
    }
}
