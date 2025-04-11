package uk.nhs.digital.svg;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import javax.net.ssl.HttpsURLConnection;

public class ProxyDispatcher extends HttpServlet {

    public final int proxy(HttpServletRequest request,
                      HttpServletResponse response,
                      URL url,
                      ThrowingBiConsumer<InputStream, HttpServletResponse, IOException> proxyResponseTransformer) throws IOException {
        HttpURLConnection proxyTarget = getConnection(url);
        forwardRequestHeadersToProxyTarget(request, proxyTarget);

        // Send proxy request and capture the response code
        int responseCode = proxyTarget.getResponseCode();

        // Return the headers from the proxy target.
        returnResponseHeadersFromProxyTarget(response, proxyTarget);
        response.setHeader("target-url", proxyTarget.getURL().toString());
        response.setHeader("target-response-code", String.valueOf(responseCode));
        response.setStatus(responseCode);

        // Return the proxy target body with the SVG's colour(s) replaced.
        InputStream proxyTargetInputStream = proxyTarget.getInputStream();
        if (proxyTargetInputStream != null) {
            forwardBodyApplyTransformer(proxyTargetInputStream, response, proxyResponseTransformer);
        } else {
            forwardBody(proxyTarget.getErrorStream(), response);
        }
        return responseCode;
    }

    private static HttpURLConnection getConnection(URL url) throws IOException {
        if (url.getProtocol().equalsIgnoreCase("https")) {
            return (HttpsURLConnection) url.openConnection();
        } else {
            return (HttpURLConnection) url.openConnection();
        }
    }

    private static void forwardRequestHeadersToProxyTarget(HttpServletRequest request, HttpURLConnection proxyConnection) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            proxyConnection.setRequestProperty(name, request.getHeader(name));
        }
    }

    private static void returnResponseHeadersFromProxyTarget(HttpServletResponse response, HttpURLConnection proxyTarget) {
        for (final String key : proxyTarget.getHeaderFields().keySet()) {
            if (key != null && !key.equalsIgnoreCase("Content-Length")) {
                response.setHeader(key, proxyTarget.getHeaderField(key));
            }
        }
    }

    public final void forwardBody(final InputStream stream, HttpServletResponse response) throws IOException {
        forwardBodyApplyTransformer(stream, response, (in, out) -> {
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(in));
            ServletOutputStream outputStream = out.getOutputStream();

            String inputLine;
            while ((inputLine = inputStream.readLine()) != null) {
                outputStream.println(inputLine);
            }

            inputStream.close();
            outputStream.flush();
        });
    }


    private static void forwardBodyApplyTransformer(InputStream stream,
                                                    HttpServletResponse response,
                                                    ThrowingBiConsumer<InputStream, HttpServletResponse, IOException> transformer) throws IOException {
        transformer.accept(stream, response);
    }

}
