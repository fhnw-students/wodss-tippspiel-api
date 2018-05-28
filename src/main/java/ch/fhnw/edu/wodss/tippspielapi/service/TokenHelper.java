package ch.fhnw.edu.wodss.tippspielapi.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class TokenHelper {

  @Value("${app.name}")
  private String APP_NAME;

  @Value("${security.jwt.secret}")
  private String SECRET;

  private int EXPIRES_IN = 60 * 60;

  private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

  public String getUsernameFromToken(String token) {
    String username;
    try {
      final Claims claims = getClaimsFromToken(token);
      username = claims.getSubject();
    } catch (Exception e) {
      username = null;
    }
    return username;
  }

  public String generateToken(String username) {
    String jws = Jwts.builder()
        .setIssuer(APP_NAME)
        .setSubject(username)
        .setIssuedAt(generateCurrentDate())
        .setExpiration(generateExpirationDate())
        .signWith(SIGNATURE_ALGORITHM, SECRET)
        .compact();
    return jws;
  }

  public Claims getClaimsFromToken(String token) {
    Claims claims;
    try {
      claims = Jwts.parser()
          .setSigningKey(this.SECRET)
          .parseClaimsJws(token)
          .getBody();
    } catch (Exception e) {
      claims = null;
    }
    return claims;
  }

  private long getCurrentTimeMillis() {
    return Calendar.getInstance().getTimeInMillis();
  }

  private Date generateCurrentDate() {
    return new Date(getCurrentTimeMillis());
  }

  private Date generateExpirationDate() {
    return new Date(getCurrentTimeMillis() + this.EXPIRES_IN * 1000);
  }
}