package com.appsdeveloperblog.app.ws.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.appsdeveloperblog.app.ws.ui.model.request.UserLoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  public AuthenticationFilter(final AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(final HttpServletRequest req,
      final HttpServletResponse res) throws AuthenticationException {
    try {

      final UserLoginRequestModel creds =
          new ObjectMapper().readValue(req.getInputStream(), UserLoginRequestModel.class);

      // @formatter:off
      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
          );
      // @formatter:on

    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(final HttpServletRequest req,
      final HttpServletResponse res, final FilterChain chain, final Authentication auth)
      throws IOException, ServletException {

    final String userName = ((User) auth.getPrincipal()).getUsername();

    // @formatter:off
      final String token = Jwts.builder()
              .setSubject(userName)
              .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
              .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET )
              .compact();
      // @formatter:on      

    res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
  }

}
