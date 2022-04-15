package uk.nhs.digital.svg.colour;

import static java.text.MessageFormat.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.svg.ProxyDispatcher;
import uk.nhs.digital.svg.SvgProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @deprecated
 * For future SVG usage, this class should not be used. Please upload svg images on the repository.
 * <p> Use {@link SvgProvider#getSvgXmlFromBean(org.hippoecm.hst.content.beans.standard.HippoBean)} instead.
 */
@Deprecated
public class SvgColourMagicServlet extends ProxyDispatcher {

    private static final Logger log = LoggerFactory.getLogger(SvgColourMagicServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final AsyncContext asyncContext = request.startAsync();
        new Thread(() -> {
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

            try {
                proxy(request, response, new URL(requestUrl), (in, out) -> {
                    BufferedReader inputStream = new BufferedReader(new InputStreamReader(in));
                    ServletOutputStream outputStream = response.getOutputStream();

                    String inputLine;
                    while ((inputLine = inputStream.readLine()) != null) {
                        outputStream.println(SvgColourMagic.replaceColour(inputLine, getColour(request)));
                    }

                    inputStream.close();
                    outputStream.flush();
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            asyncContext.complete();
        }).start();
    }

    private static String getColour(HttpServletRequest request) {
        final String colour = request.getParameter("colour");
        if (colour != null && SvgColourMagic.isColour(colour)) {
            return format("#{0}", colour);
        }
        return format("#{0}", "000");
    }

}
