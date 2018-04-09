package ch.fhnw.edu.wodss.tippspielapi.TippspielApi.config;

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

/**
 * This Filter adds an {@link org.springframework.security.core.Authentication} (namely, a {@link
 * JwtAuthenticationToken}) to the security context (in case of presence of the "Authorization
 * Bearer" header) so that the {@link JwtAuthenticationProvider} can check the token against the
 * users in the database and verify that the user (that belongs to the token) is allowed to access
 * the resource.
 */
@Component
public class JwtAuthFilter implements Filter {

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
    if (authorization != null && authorization.contains(BEARER)) {
      String token = authorization.replaceAll(BEARER + " ", "");
      JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(token);
      SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
    }
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
  }
}
