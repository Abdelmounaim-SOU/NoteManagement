package com.example.notemanagment.Controllers;

import com.example.notemanagment.Models.*;
import com.example.notemanagment.Models.Module;
import com.example.notemanagment.Repository.*;
import jakarta.servlet.http.HttpSession;
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
public class ProfHomeController {

    @Autowired
    private HttpSession session;

    @Autowired
    private ModuleElementRepository moduleElementRepo;

    @Autowired
    private ProfRepo profRepo;
    @Autowired
    private StudentRepo studentRepo;



    @GetMapping("prof")
    public String showProfDashboard(Model model) {
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            System.out.println("Error: User ID is null.");
            return "error"; // Redirect to an error page
        }

        System.out.println("Successfully retrieved the ID: " + userId);

        // Fetch professor details
        Optional<Professor> profOptional = profRepo.findById(userId);
        if (profOptional.isEmpty()) {
            System.out.println("Error: No professor found with ID " + userId);
            return "error"; // Redirect to an error page if the professor is not found
        }

        Professor prof = profOptional.get();

        System.out.println("Professor Name: " + prof.getName());

        // Fetch module elements associated with this professor
        List<ModuleElement> moduleElements = moduleElementRepo.findByProfessorId(userId);

        // Log the number of module elements
        System.out.println("Number of module elements: " + moduleElements.size());

        // Fetch evaluations for each module element
        for (ModuleElement element : moduleElements) {
            List<Evaluation> evaluations = element.getEvaluations();
            element.setEvaluations(evaluations); // Ensure evaluations are populated
        }

        // Add data to the model
        model.addAttribute("userId", userId);
        model.addAttribute("username", prof.getName());
        model.addAttribute("elements", moduleElements);

        return "Dashboard/prof/prof";
    }


    // get to edit the notes of your class:
    @GetMapping("/listEtudiants")
    public String listEtudiants(@RequestParam("elementId") Long elementId, Model model) {
        // Existing logic
        return "Dashboard/prof/listEtudiants";
    }
    @GetMapping("/elementDetails")
    public String showElementDetails(@RequestParam("elementId") Long elementId, Model model) {
        // Find the module element
        Optional<ModuleElement> elementOptional = moduleElementRepo.findById(elementId);
        if (elementOptional.isEmpty()) {
            System.out.println("Error: No module element found with ID " + elementId);
            return "error";
        }
        ModuleElement element = elementOptional.get();

        // Find students in the same field and semester
        Field field = element.getModule().getField();
        Semester semester = element.getModule().getSemester();
        List<Student> students = studentRepo.findByFieldAndSemester(field, semester);

        // Calculate average note for each student
        Map<Student, Double> studentAverages = new HashMap<>();
        for (Student student : students) {
            double totalWeightedNote = 0.0;
            double totalCoefficient = 0.0;

            for (Evaluation evaluation : element.getEvaluations()) {
                Note note = student.getNotes().stream()
                        .filter(n -> n.getEvaluation().getId().equals(evaluation.getId()))
                        .findFirst().orElse(null);

                if (note != null) {
                    totalWeightedNote += (evaluation.getCoefficient() / 100.0) * note.getValue();
                    totalCoefficient += evaluation.getCoefficient();
                }
            }

            double averageNote = totalCoefficient > 0 ? totalWeightedNote  : 0.0;
            studentAverages.put(student, averageNote);
        }

        // Add data to the model
        model.addAttribute("element", element);
        model.addAttribute("studentAverages", studentAverages);

        return "Dashboard/prof/elementDetails";
    }


}