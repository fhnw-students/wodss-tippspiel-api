package ch.fhnw.edu.wodss.tippspielapi.model;

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

  @Column(nullable = false, unique = true, length = 25)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, columnDefinition = "bit(1) DEFAULT b'0'")
  private boolean admin;

  @Transient
  private String token;

  @Column
  private String verificationToken;

  @Column
  private String resetToken;

  public void clearToken() {
    token = null;
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
        Objects.equals(admin, user.admin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, email, password, admin);
  }


  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", admin=" + admin +
            ", token='" + token + '\'' +
            ", verificationToken='" + verificationToken + '\'' +
            ", resetToken='" + resetToken + '\'' +
            '}';
  }
}
