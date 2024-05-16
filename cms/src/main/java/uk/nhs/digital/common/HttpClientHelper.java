package uk.nhs.digital.common;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;

public class HttpClientHelper {

    private static final Logger log = LoggerFactory.getLogger(HttpClientHelper.class);
    private static final int TIMEOUT_IN_SECS = 10;

    public HttpResponse resolveFinalDestination(final String link) {

        int statusCode;
        String finalDestination;

        try (CloseableHttpClient httpclient = HttpClients.custom()
            .setDefaultRequestConfig(getTimeoutConfig(TIMEOUT_IN_SECS))
            .setRedirectStrategy(new LaxRedirectStrategy())
            .build()) {

            HttpClientContext context = HttpClientContext.create();
            HttpGet httpGet = new HttpGet(link);
            CloseableHttpResponse response = httpclient.execute(httpGet, context);
            statusCode = response.getStatusLine().getStatusCode();

            log.warn("[" + statusCode + "] " + link);

            HttpHost target = context.getTargetHost();
            List<URI> redirectLocations = context.getRedirectLocations();
            URI location = URIUtils.resolve(httpGet.getURI(), target, redirectLocations);
            finalDestination = location.toASCIIString();
        } catch (Exception ex) {
            statusCode = 500;
            finalDestination = ex.getMessage();
        }
        return new HttpResponse(statusCode, finalDestination) ;
    }

    public int getInitialHttpStatusCode(String link) {

        int statusCode;
        try (CloseableHttpClient httpclient = HttpClients.custom()
            .setDefaultRequestConfig(getTimeoutConfig(TIMEOUT_IN_SECS))
            .disableRedirectHandling()
            .build()) {

            HttpClientContext context = HttpClientContext.create();
            HttpGet httpGet = new HttpGet(link);
            CloseableHttpResponse response = httpclient.execute(httpGet, context);
            statusCode = response.getStatusLine().getStatusCode();
        } catch (Exception ex) {
            statusCode = 500;
        }
        return statusCode;
    }

    private RequestConfig getTimeoutConfig(int seconds) {
        return RequestConfig.custom().setConnectTimeout(seconds * 1000).build();
    }

    public final class HttpResponse {
        private final int statusCode;
        private final String url;

        public HttpResponse(int statusCode, String url) {
            this.statusCode = statusCode;
            this.url = url;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getUrl() {
            return url;
        }
    }
}

