package ch.fhnw.edu.wodss.tippspielapi.config.authentication;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationPathFilter implements Filter {

  public static final String BEARER = "Bearer";
  public static final String AUTHORIZATION = "Authorization";
  public static final String AUTH_LOGIN_PATH = "/auth/login";
  public static final String API_PATH = "/api";
  public static final String BASIC = "Basic";
  private static final String AUTH_REGISTRATION_PATH = "/auth/register";

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest servletRequest = (HttpServletRequest) request;
    String authorization = servletRequest.getHeader(AUTHORIZATION);
    String contextPath = servletRequest.getServletPath();
    if (!canAccess(contextPath, authorization)) {
      ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
    chain.doFilter(request, response);
  }

  private boolean canAccess(String contextPath, String authorization) {
    boolean isApiRequested = contextPath.contains(API_PATH);
    boolean isLoginRequested = contextPath.contains(AUTH_LOGIN_PATH);
    boolean isRegistrationRequested = contextPath.contains(AUTH_REGISTRATION_PATH);
    if (authorization == null || isApiRequested || isRegistrationRequested) {
      return true;
    } else if (authorization.contains(BASIC)) {
      return isLoginRequested;
    } else {
      return authorization.contains(BEARER) && !isLoginRequested;
    }
  }

  @Override
  public void destroy() {
  }
}
