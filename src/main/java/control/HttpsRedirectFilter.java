package control;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class HttpsRedirectFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpRequest.getScheme().equals("http")) {
            String redirectUrl = "https://" + httpRequest.getServerName() + ":" + 8443 + httpRequest.getRequestURI();
            httpResponse.sendRedirect(redirectUrl);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {}
}
