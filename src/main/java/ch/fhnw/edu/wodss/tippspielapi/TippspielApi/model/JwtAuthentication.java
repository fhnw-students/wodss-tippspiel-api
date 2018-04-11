package ch.fhnw.edu.wodss.tippspielapi.TippspielApi.model;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class JwtAuthentication {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String token;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date expiration;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "user_id")
  @MapsId
  private User user;

  public JwtAuthentication() {
  }

  public JwtAuthentication(User user) {
    this.user = user;
    generateNewToken();
  }

  public String getToken() {
    return token;
  }

  public Date getExpiration() {
    return expiration;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public void setExpiration(Date expiration) {
    this.expiration = expiration;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Creates a new token that expires in an hour.
   */
  public void generateNewToken() {
    this.token = UUID.randomUUID().toString();
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.HOUR, 1);
    this.expiration = calendar.getTime();
  }
}
