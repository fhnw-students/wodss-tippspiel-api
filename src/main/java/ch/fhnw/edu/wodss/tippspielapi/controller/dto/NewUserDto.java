package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class NewUserDto {

  @Min(3)
  @Max(25)
  private String username;

  private String email;
  private String password;

  public NewUserDto() {

  }

  public NewUserDto(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
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
}
