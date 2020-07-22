package uk.nhs.digital.svg.stylesheet;

import java.io.IOException;
import javax.servlet.*;

public class SvgStylesheetFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("before");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.printf("After");
    }

    @Override
    public void destroy() {
        /* Nothing to clean up or close */
    }

}
