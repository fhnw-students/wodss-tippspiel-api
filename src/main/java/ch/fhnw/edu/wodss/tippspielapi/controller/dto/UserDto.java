package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.User;

public class UserDto {

    private String username;
    private String email;
    private Boolean isAdmin;

    public UserDto() {

    }

    public UserDto(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.isAdmin = user.getIsAdmin();
    }


    public UserDto(String username, String email, Boolean isAdmin) {
        this.username = username;
        this.email = email;
        this.isAdmin = isAdmin;
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

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}