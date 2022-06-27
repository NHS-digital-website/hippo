package uk.nhs.digital.svg.library;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.svg.NhsDigitalColours;
import uk.nhs.digital.svg.ProxyDispatcher;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IconDownloadServlet extends ProxyDispatcher {

    private static final Logger log = LoggerFactory.getLogger(IconDownloadServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String type = request.getParameter("type");
        String colour = request.getParameter("colour");
        String name = request.getParameter("name");

        try {
            if (type == null
                || type.isEmpty()
                || colour == null
                || colour.isEmpty()
                || name == null
                || name.isEmpty()
            ) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing query parameter in {type, colour, name}");
                return;
            }
            String url = url(request, NhsDigitalColours.fromString(colour));

            log.debug(String.format("IconDownloadServlet will proxy %s", url));

            int r = proxy(request, response, new URL(url), (in, out) -> {
                if (type.equalsIgnoreCase("png")) {
                    response.setHeader("Content-Disposition", contentDisposition(type, colour, name));
                    try (OutputStream png = response.getOutputStream()) {
                        new PNGTranscoder().transcode(new TranscoderInput(in), new TranscoderOutput(png));
                    } catch (TranscoderException e) {
                        log.error("IconDownloadServlet Transcoder error", e);
                    }
                } else {
                    response.setHeader("Content-Disposition", contentDisposition(type, colour, name));
                    try {
                        forwardBody(in, out);
                    } catch (IOException ioe) {
                        log.error("IOException: ", ioe);
                    }
                }
            });
            log.debug(String.format("IconDownloadServlet got a %d response from %s", r, url));
        } catch (IOException ioe) {
            log.error("IOException: ", ioe);
        }

    }

    private static String url(HttpServletRequest request, NhsDigitalColours colour) {
        String url = request.getRequestURL().toString();

        if (url.contains("8080")) {
            // LOCALHOST Site icons AND LIVE/TST/UAT/DEV/TRAINING CMS preview icons
            url = url.replace("/download-icon/binaries", "/svg-magic/binaries");
        } else {
            // LIVE/TST/UAT/DEV/TRAINING Site icons
            url = url.replace("/site/download-icon/binaries", "/svg-magic/binaries");
        }

        return String.format("%s?colour=%s", url, colour.getHex());
    }

    private String contentDisposition(String type, String colour, String name) {
        return String.format("attachment; filename=\"%s.%s\"", fileName(name, type, NhsDigitalColours.fromString(colour)), type.toLowerCase());
    }

    private static String fileName(final String name, final String type, NhsDigitalColours colour) {
        String n = name.substring(0, 1).toUpperCase() + name.substring(1);
        String t = type.toUpperCase();
        String c = colour.formattedName();
        return String.format("%s %s %s", n, t, c);
    }
}
