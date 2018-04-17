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
  @Temporal(TemporalType.TIMESTAMP)
  private Date expiration;

  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  Set<TeamMate> teamMates = new HashSet<>();


  /**
   * Creates a new token that expires in an hour.
   */
  public void generateNewToken() {
    this.token = UUID.randomUUID().toString();
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.HOUR, 1);
    this.expiration = calendar.getTime();
  }

  /**
   * Determines whether this user's token has expired.
   */
  public boolean hasTokenExpired() {
    Calendar now = Calendar.getInstance();
    return expiration == null || expiration.before(now.getTime());
  }

  public void clearToken() {
    this.token = null;
    this.expiration = null;
  }

  public void generateVerificationToken() {
    this.verificationToken = UUID.randomUUID().toString();
  }

  public void clearVerificationToken() {
    verificationToken = null;
  }

  public boolean isNotVerified() {
    return !(verificationToken == null || verificationToken.isEmpty());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean getAdmin() {
    return isAdmin;
  }

  public void setAdmin(Boolean admin) {
    isAdmin = admin;
  }

  public Set<TeamMate> getTeamMates() {
    return teamMates;
  }

  public void setTeamMates(Set<TeamMate> teamMates) {
    this.teamMates = teamMates;
  }

  public String getToken() {
    return token;
  }

  public Date getExpiration() {
    return (Date) expiration.clone();
  }

  public String getVerificationToken() {
    return verificationToken;
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
