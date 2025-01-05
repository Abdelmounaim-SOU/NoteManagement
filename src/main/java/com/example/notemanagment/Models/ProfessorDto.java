package com.example.notemanagment.Models;

import jakarta.validation.constraints.NotEmpty;

public class ProfessorDto {
    @NotEmpty(message = "The username is required")
    private String name;
    @NotEmpty(message = "The username is required")
    private String surname;
    @NotEmpty(message = "The password is required")
    private String password;
    @NotEmpty(message = "The sprcialty is required")
    private String specialty;
    @NotEmpty(message = "The code is required")
    private String code;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}

