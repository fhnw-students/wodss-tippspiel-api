package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, columnDefinition = "bit(1) DEFAULT b'0'")
  private Boolean isAdmin;

  @Column(unique = true)
  private String token;

  @Column
  private String verificationToken;

  @Column
  private String resetToken;

  @Column
  @Temporal(TemporalType.TIMESTAMP)
  private Date expiration;

  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  Set<TeamMate> teamMates = new HashSet<>();


  /**
   * Creates a new token that expires in an hour.
   */
  public void generateNewAuthenticationToken() {
    token = UUID.randomUUID().toString();
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.HOUR, 1);
    expiration = calendar.getTime();
  }

  /**
   * Determines whether this user's token has expired.
   */
  public boolean hasAuthenticationTokenExpired() {
    Calendar now = Calendar.getInstance();
    return expiration == null || expiration.before(now.getTime());
  }

  public void clearToken() {
    token = null;
    expiration = null;
  }

  public void generateVerificationToken() {
    verificationToken = UUID.randomUUID().toString();
  }

  public void clearVerificationToken() {
    verificationToken = null;
  }

  public boolean isNotVerified() {
    return !isVerified();
  }

  public boolean isVerified() {
    return verificationToken == null || verificationToken.isEmpty();
  }

  public void generateResetToken() {
    resetToken = UUID.randomUUID().toString();
  }

  public void clearResetToken() {
    resetToken = null;
  }

  public boolean isNotResetting() {
    return resetToken == null || resetToken.isEmpty();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof User)) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(id, user.id) &&
        Objects.equals(username, user.username) &&
        Objects.equals(email, user.email) &&
        Objects.equals(password, user.password) &&
        Objects.equals(isAdmin, user.isAdmin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, email, password, isAdmin);
  }
}
