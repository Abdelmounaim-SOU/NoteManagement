package com.example.notemanagment.Controllers;

import com.example.notemanagment.Models.*;
import com.example.notemanagment.Models.Module;
import com.example.notemanagment.Repository.ModuleElementRepository;
import com.example.notemanagment.Repository.ProfRepo;
import com.example.notemanagment.Repository.StudentRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
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
        } else {
            System.out.println("Succesfully retreived the ID :" + userId);
        }

        System.out.println("User ID: " + userId);

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
        System.out.println("Number of module elements: " + moduleElements.size());

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
}