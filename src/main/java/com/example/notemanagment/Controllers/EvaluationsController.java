package com.example.notemanagment.Controllers;

import com.example.notemanagment.Models.Evaluation;
import com.example.notemanagment.Models.ModuleElement;
import com.example.notemanagment.Repository.EvaluationRepo;

import com.example.notemanagment.Repository.ModuleElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/Dashboard/admin")
public class EvaluationsController {

    @Autowired
    private ModuleElementRepository moduleElementRepo;

    @Autowired
    private EvaluationRepo evaluationRepo;

    // Show evaluations for a module element
    @GetMapping("/elementEvaluations/{elementId}")
    public String showElementEvaluations(@PathVariable Long elementId, Model model) {
        ModuleElement moduleElement = moduleElementRepo.findById(elementId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid module element ID"));

        List<Evaluation> evaluations = moduleElement.getEvaluations();

        model.addAttribute("moduleElement", moduleElement);
        model.addAttribute("evaluations", evaluations);

        return "Dashboard/admin/elementEvaluations";
    }

    // Add new evaluation to a module element
    @PostMapping("/addEvaluation/{elementId}")
    public String addEvaluation(
            @PathVariable Long elementId,
            @ModelAttribute("evaluation") Evaluation evaluation,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            // If validation fails, return to the same page with errors
            return "Dashboard/admin/elementEvaluations";
        }

        // Find the ModuleElement by ID
        ModuleElement element = moduleElementRepo.findById(elementId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid element ID"));

        // Associate the evaluation with the element
        evaluation.setModuleElement(element);

        // Save the evaluation
        evaluationRepo.save(evaluation);

        // Redirect to the evaluations page for the element
        redirectAttributes.addFlashAttribute("successMessage", "Evaluation added successfully!");
        return "redirect:/Dashboard/admin/elementEvaluations/" + elementId;
    }

}
