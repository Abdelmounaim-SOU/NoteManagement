package com.example.notemanagment.Controllers;

import com.example.notemanagment.Models.*;
import com.example.notemanagment.Repository.EvaluationRepo;
import com.example.notemanagment.Repository.NoteRepo;
import com.example.notemanagment.Repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/Dashboard/prof")
public class evaluationDetails_Controller {
    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private EvaluationRepo evaluationRepo ;

    @Autowired
    private NoteRepo noteRepo ;
    @GetMapping("/evaluationDetails")
    public String evaluationDetails(@RequestParam("evaluationId") Long evaluationId, Model model) {
        Optional<Evaluation> evaluationOptional = evaluationRepo.findById(evaluationId);
        if (evaluationOptional.isEmpty()) {
            System.out.println("Error: No evaluation found with ID " + evaluationId);
            return "error";
        }

        Evaluation evaluation = evaluationOptional.get();
        ModuleElement moduleElement = evaluation.getModuleElement();

        List<Student> students = studentRepo.findByFieldAndSemester(
                moduleElement.getModule().getField(),
                moduleElement.getModule().getSemester()
        );

        // Prepare student notes
        List<Map<String, Object>> studentNotes = students.stream().map(student -> {
            Note note = noteRepo.findByStudentAndEvaluation(student, evaluation).orElse(null);
            Map<String, Object> studentData = new HashMap<>();
            studentData.put("id", student.getId());
            studentData.put("name", student.getName());
            studentData.put("surname", student.getSurname());
            studentData.put("note", note != null ? note.getValue() : 0);
            return studentData;
        }).toList();

        model.addAttribute("evaluation", evaluation);
        model.addAttribute("students", studentNotes);

        return "Dashboard/prof/evaluationDetails";
    }


    @PostMapping("/saveMarks")
    public ResponseEntity<String> saveMarks(@RequestBody List<MarkSubmission> submissions) {
        for (MarkSubmission submission : submissions) {
            // Fetch student and evaluation
            Student student = studentRepo.findById(submission.getStudentId())
                    .orElse(null);
            Evaluation evaluation = evaluationRepo.findById(submission.getEvaluationId())
                    .orElse(null);

            if (student == null || evaluation == null) {
                System.out.println("Error: Missing student or evaluation for submission: " + submission);
                continue; // Skip invalid submissions
            }

            // Check if the mark is between 0 and 20
            if (submission.getMark() != null && (submission.getMark() < 0 || submission.getMark() > 20)) {
                return ResponseEntity.badRequest().body("Marks must be between 0 and 20 for student ID: " + submission.getStudentId());
            }

            // Fetch existing note or create a new one
            Note note = noteRepo.findByStudentAndEvaluation(student, evaluation).orElse(new Note());
            note.setStudent(student);
            note.setEvaluation(evaluation);

            // Update note value only if submission contains a valid mark
            if (submission.getMark() != null) {
                note.setValue(submission.getMark());
            } else if (note.getValue() == null) {
                note.setValue(0.0); // Default to 0 if no value exists
            }

            // Save the note
            noteRepo.save(note);
        }
        return ResponseEntity.ok("Marks saved successfully!");
    }

    @PostMapping("/saveDefinitely")
    public ResponseEntity<String> saveDefinitely(@RequestParam("evaluationId") Long evaluationId) {
        // Fetch the evaluation
        Optional<Evaluation> evaluationOptional = evaluationRepo.findById(evaluationId);
        if (evaluationOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Evaluation not found");
        }

        Evaluation evaluation = evaluationOptional.get();

        // Check if already validated
        if (evaluation.isValidated()) {
            return ResponseEntity.badRequest().body("Evaluation already finalized");
        }

        // Set validated to true
        evaluation.setValidated(true);
        evaluationRepo.save(evaluation);

        return ResponseEntity.ok("Evaluation finalized successfully!");
    }




}
