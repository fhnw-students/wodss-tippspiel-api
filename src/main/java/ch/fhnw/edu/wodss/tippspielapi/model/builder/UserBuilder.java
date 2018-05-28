package ch.fhnw.edu.wodss.tippspielapi.model.builder;

import ch.fhnw.edu.wodss.tippspielapi.model.User;

public class UserBuilder {

  private String email;
  private String username;
  private boolean admin;
  private Long id;

  public UserBuilder withEmail(String email) {
    this.email = email;
    return this;
  }

  public UserBuilder withUsername(String username) {
    this.username = username;
    return this;
  }

  public UserBuilder withAdmin(boolean admin) {
    this.admin = admin;
    return this;
  }

  public UserBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public User build() {
    User user = new User();
    user.setEmail(email);
    user.setAdmin(admin);
    user.setUsername(username);
    user.setId(id);
    return user;
  }
}
