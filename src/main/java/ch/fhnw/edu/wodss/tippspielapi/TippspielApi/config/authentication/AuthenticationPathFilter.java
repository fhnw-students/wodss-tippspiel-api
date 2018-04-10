package ch.fhnw.edu.wodss.tippspielapi.TippspielApi.config.authentication;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationPathFilter implements Filter {

  public static final String BEARER = "Bearer";
  public static final String AUTHORIZATION = "Authorization";

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest servletRequest = (HttpServletRequest) request;
    String authorization = servletRequest.getHeader(AUTHORIZATION);
    String contextPath = servletRequest.getServletPath();
    boolean servletPathIsAuthLogin =
        contextPath.contains("/auth/login") || contextPath.contains("/auth/login/");
    boolean isTokenAuth = authorization.contains(BEARER);
    boolean isAuthLoginPathWithBearerToken = servletPathIsAuthLogin && isTokenAuth;
    boolean isBasicAuth = authorization.contains("Basic Auth");
    boolean isNotAuthLoginPathWithBasicAuth = !servletPathIsAuthLogin && isBasicAuth;
    if (isAuthLoginPathWithBearerToken || isNotAuthLoginPathWithBasicAuth) {
      SecurityContextHolder.getContext().setAuthentication(null);
    }
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
  }
}
