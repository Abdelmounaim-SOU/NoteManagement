package com.example.notemanagment.Models;

public class MarkSubmission {
    private Long studentId;
    private Long evaluationId;
    private Double mark;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Long evaluationId) {
        this.evaluationId = evaluationId;
    }

    public Double getMark() {
        return mark;
    }

    public void setMark(Double mark) {
        this.mark = mark;
    }

    // Getters and Setters
}
