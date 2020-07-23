package uk.nhs.digital.svg.stylesheet;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

public class SvgStylesheetFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        CharResponseWrapper wrapper = new CharResponseWrapper((HttpServletResponse) servletResponse);

        filterChain.doFilter(servletRequest, servletResponse);

        PrintWriter responseWriter = servletResponse.getWriter();

        if (wrapper.getContentType().contains("text/html")) {
            CharArrayWriter charWriter = new CharArrayWriter();
            String originalContent = wrapper.toString();

            int indexOfCloseBodyTag = originalContent.indexOf("</body>") - 1;

            charWriter.write(originalContent.substring(0, indexOfCloseBodyTag));

            String copyrightInfo = "<p>Copyright CodeJava.net</p>";
            String closeHtmlTags = "</body></html>";

            charWriter.write(copyrightInfo);
            charWriter.write(closeHtmlTags);

            String alteredContent = charWriter.toString();
            servletResponse.setContentLength(alteredContent.length());
            responseWriter.write(alteredContent);
        }
    }

    @Override
    public void destroy() {
        /* Nothing to clean up or close */
    }

}
