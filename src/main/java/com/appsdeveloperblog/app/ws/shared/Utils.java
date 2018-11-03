package com.appsdeveloperblog.app.ws.shared;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;
import org.springframework.stereotype.Component;
import com.appsdeveloperblog.app.ws.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class Utils {

  private final Random RANDOM = new SecureRandom();
  private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

  public String generateUserId(final int length) {
    return generateRandomString(length);
  }

  public String generateAddressId(final int length) {
    return generateRandomString(length);
  }

  private String generateRandomString(final int length) {
    final StringBuilder returnValue = new StringBuilder(length);

    for (int i = 0; i < length; i++) {
      returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
    }
    return new String(returnValue);
  }

  public static boolean hasTokenExpired(String token) {
    boolean returnValue = false;

    try {
      Claims claims = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret())
          .parseClaimsJws(token).getBody();

      Date tokenExpirationDate = claims.getExpiration();
      Date todayDate = new Date();

      returnValue = tokenExpirationDate.before(todayDate);
    } catch (ExpiredJwtException ex) {
      returnValue = true;
    }

    return returnValue;
  }

  public static String generateEmailVerificationToken(String userId) {
    // @formatter:off
    String token = Jwts.builder()
                    .setSubject(userId)
                    .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                    .compact();
    // @formatter:on
    return token;
  }
}
