package com.example.notemanagment.Models;


import jakarta.validation.constraints.*;


public class UserDto {
    @NotEmpty(message = "The username is required")
    private String username;
    @NotEmpty(message = "The password is required")
    private String password;
    @NotEmpty(message = "The role is required")
    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
