package uk.nhs.digital.svg.colour;

import static java.text.MessageFormat.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.function.Function;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SvgColourSetterServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(SvgColourSetterServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String requestUrl = request.getRequestURL().toString();

        log.debug("SvgColourSetterServlet requestURL - " + requestUrl);

        //  Set up proxy request
        if (requestUrl.contains("8080")) {
            // LOCALHOST Site icons AND LIVE/TST/UAT/DEV/TRAINING CMS preview icons
            requestUrl = requestUrl.replace("/svg-magic", "");
        } else {
            // LIVE/TST/UAT/DEV/TRAINING Site icons
            requestUrl = requestUrl.replace("/site/svg-magic", "");
        }

        HttpURLConnection proxyTarget = getConnection(new URL(requestUrl));
        forwardRequestHeadersToProxyTarget(request, proxyTarget);

        // Send proxy request and capture the response code
        int responseCode = proxyTarget.getResponseCode();

        // Return the headers from the proxy target.
        returnResponseHeadersFromProxyTarget(response, proxyTarget);
        response.setHeader("target-url", proxyTarget.getURL().toString());
        response.setHeader("target-response-code", String.valueOf(responseCode));
        response.setStatus(responseCode);

        // Return the proxy target body with the SVG's colour(s) replaced.
        if (responseCode >= 200 && responseCode <= 299) {
            forwardBodyAndApplyFunction(proxyTarget.getInputStream(), response, s -> SvgColourSetter.replaceColour(s, getColour(request)));
        } else {
            forwardBody(proxyTarget.getErrorStream(), response);
        }
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

    private static String getColour(HttpServletRequest request) {
        final String colour = request.getParameter("colour");
        if (colour != null && SvgColourSetter.isColour(colour)) {
            return format("#{0}", colour);
        }
        return format("#{0}", "000");

    }

    private static void forwardBody(final InputStream stream, HttpServletResponse response) throws IOException {
        forwardBodyAndApplyFunction(stream, response, passesThought -> passesThought);
    }

    private static void forwardBodyAndApplyFunction(final InputStream stream, HttpServletResponse response, Function<String, String> magic) throws IOException {
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(stream));
        ServletOutputStream outputStream = response.getOutputStream();

        String inputLine;
        while ((inputLine = inputStream.readLine()) != null) {
            outputStream.println(magic.apply(inputLine));
        }

        inputStream.close();
        outputStream.flush();
    }
}
