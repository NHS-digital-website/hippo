package uk.nhs.digital.common;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ResolveLinksHelper {

    public String resolveFinalDestination(String link) throws IOException, URISyntaxException {

        CloseableHttpClient httpclient = HttpClients.custom()
            .setRedirectStrategy(new LaxRedirectStrategy())
            .build();

        String finalDestination;

        try {
            HttpClientContext context = HttpClientContext.create();
            HttpGet httpGet = new HttpGet(link);
            httpclient.execute(httpGet, context);
            HttpHost target = context.getTargetHost();
            List<URI> redirectLocations = context.getRedirectLocations();
            URI location = URIUtils.resolve(httpGet.getURI(), target, redirectLocations);
            finalDestination = location.toASCIIString();
        } finally {
            httpclient.close();
        }
        return finalDestination;
    }

    public int getInitialHttpStatusCode(String link) throws IOException {

        int statusCode;
        try (CloseableHttpClient httpclient = HttpClients.custom()
            .disableRedirectHandling()
            .build()) {

            HttpClientContext context = HttpClientContext.create();
            HttpGet httpGet = new HttpGet(link);
            CloseableHttpResponse response = httpclient.execute(httpGet, context);
            statusCode = response.getStatusLine().getStatusCode();
        }

        return statusCode;

    }
}
