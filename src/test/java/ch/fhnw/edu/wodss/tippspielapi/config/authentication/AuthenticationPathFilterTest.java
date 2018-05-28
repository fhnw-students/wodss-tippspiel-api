package ch.fhnw.edu.wodss.tippspielapi.config.authentication;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import ch.fhnw.edu.wodss.tippspielapi.config.authentication.AuthenticationPathFilter;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class AuthenticationPathFilterTest {

  @Test
  public void testFilterLoginWithBasicAuth() throws IOException, ServletException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();
    FilterChain chain = new MockFilterChain();

    request.setServletPath(AuthenticationPathFilter.AUTH_LOGIN_PATH);
    request.addHeader(AuthenticationPathFilter.AUTHORIZATION, "Basic a2VuOjEyMzQ=");

    AuthenticationPathFilter filter = new AuthenticationPathFilter();
    filter.doFilter(request, response, chain);

    Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
  }

  @Test
  public void testFilterLoginWithBearerToken() throws IOException, ServletException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();
    FilterChain chain = new MockFilterChain();

    request.setServletPath(AuthenticationPathFilter.AUTH_LOGIN_PATH);
    request.addHeader(AuthenticationPathFilter.AUTHORIZATION,
        "Bearer 83798139-406e-4c49-8efc-5cdbaa39dd86");

    AuthenticationPathFilter filter = new AuthenticationPathFilter();
    filter.doFilter(request, response, chain);

    Assert.assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
  }

  @Test
  public void testFilterUserPathWithBasicAuth() throws IOException, ServletException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();
    FilterChain chain = new MockFilterChain();

    request.setServletPath("/user");
    request.addHeader(AuthenticationPathFilter.AUTHORIZATION, "Basic a2VuOj1yMzQ=");

    AuthenticationPathFilter filter = new AuthenticationPathFilter();
    filter.doFilter(request, response, chain);

    Assert.assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
  }

  @Test
  public void testFilterUserPathWithBearerToken() throws IOException, ServletException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();
    FilterChain chain = new MockFilterChain();

    request.setServletPath("/user");
    request.addHeader(AuthenticationPathFilter.AUTHORIZATION,
        "Bearer 13469239-506e-4b49-8edc-4cdaba39ee83");

    AuthenticationPathFilter filter = new AuthenticationPathFilter();
    filter.doFilter(request, response, chain);

    Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
  }

  @Test
  public void testFilterApiPathAnonymous() throws IOException, ServletException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();
    FilterChain chain = new MockFilterChain();

    request.setServletPath("/api");

    AuthenticationPathFilter filter = new AuthenticationPathFilter();
    filter.doFilter(request, response, chain);

    Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
  }


  @Test
  public void testFilterApiPathWithBearerToken() throws IOException, ServletException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();
    FilterChain chain = new MockFilterChain();

    request.setServletPath("/api");
    request.addHeader(AuthenticationPathFilter.AUTHORIZATION,
        "Bearer 13469239-506e-4b49-8edc-4cdaba39ee83");

    AuthenticationPathFilter filter = new AuthenticationPathFilter();
    filter.doFilter(request, response, chain);

    Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
  }

  @Test
  public void testFilterApiPathWithBasicAuth() throws IOException, ServletException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();
    FilterChain chain = new MockFilterChain();

    request.setServletPath("/api");
    request.addHeader(AuthenticationPathFilter.AUTHORIZATION, "Basic a2VuOj1yMzQ=");

    AuthenticationPathFilter filter = new AuthenticationPathFilter();
    filter.doFilter(request, response, chain);

    Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
  }
}
