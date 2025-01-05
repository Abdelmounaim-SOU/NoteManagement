package com.example.notemanagment.Models;

import jakarta.validation.constraints.NotEmpty;

public class FieldDto {
    @NotEmpty(message = "The name is required")
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
