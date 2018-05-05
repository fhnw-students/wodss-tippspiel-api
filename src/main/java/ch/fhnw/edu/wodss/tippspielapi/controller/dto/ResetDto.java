package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

public class ResetDto {

  private String email;

  public ResetDto() {
  }

  public ResetDto(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
