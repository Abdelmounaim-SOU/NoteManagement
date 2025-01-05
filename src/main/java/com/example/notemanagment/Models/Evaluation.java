package com.example.notemanagment.Models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "evaluations")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "module_element_id", nullable = false)
    private ModuleElement moduleElement;

    @Column(nullable = false)
    private String evaluationMode;

    @Column(nullable = false)
    private Double coefficient;

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ModuleElement getModuleElement() {
        return moduleElement;
    }

    public void setModuleElement(ModuleElement moduleElement) {
        this.moduleElement = moduleElement;
    }

    public String getEvaluationMode() {
        return evaluationMode;
    }

    public void setEvaluationMode(String evaluationMode) {
        this.evaluationMode = evaluationMode;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
