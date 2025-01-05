package com.example.notemanagment.Models;

public class ModuleDto {
    private Long id;
    private String name;
    private String code;
    private Long semesterId; // Updated to reference Semester by its ID
    private Integer fieldId;

    // Constructors
    public ModuleDto() {
    }

    public ModuleDto(Long id, String name, String code, Long semesterId, Integer fieldId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.semesterId = semesterId;
        this.fieldId = fieldId;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }
}
