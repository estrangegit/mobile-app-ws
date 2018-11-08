package com.appsdeveloperblog.app.ws.shared;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;
import org.springframework.stereotype.Service;
import com.appsdeveloperblog.app.ws.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
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

  public static boolean hasTokenExpired(final String token) {
    boolean returnValue = false;

    try {
      final Claims claims = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret())
          .parseClaimsJws(token).getBody();

      final Date tokenExpirationDate = claims.getExpiration();
      final Date todayDate = new Date();

      returnValue = tokenExpirationDate.before(todayDate);
    } catch (final ExpiredJwtException ex) {
      returnValue = true;
    }

    return returnValue;
  }

  public String generateEmailVerificationToken(final String userId) {
    final String token =
        generateVerificationToken(userId, SecurityConstants.EMAIL_VERIFICATION_EXPIRATION_TIME);
    return token;
  }

  public String generatePasswordResetToken(final String userId) {

    final String token =
        generateVerificationToken(userId, SecurityConstants.PASSWORD_RESET_EXPIRATION_TIME);
    return token;
  }

  private String generateVerificationToken(final String userId, final long expirationTime) {
    // @formatter:off
    final String token = Jwts.builder()
                    .setSubject(userId)
                    .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                    .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                    .compact();
    // @formatter:on
    return token;
  }

}
