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
        model.addAttribute("evaluation", new Evaluation());

        return "Dashboard/admin/elementEvaluations";
    }

    // Add new evaluation to a module element
    @PostMapping("/addEvaluation/{elementId}")
    public String addEvaluation(
            @PathVariable Long elementId,
            @ModelAttribute("evaluation") Evaluation evaluation,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        ModuleElement element = moduleElementRepo.findById(elementId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid element ID"));

        // Validation for coefficient
        if (evaluation.getCoefficient() < 0 || evaluation.getCoefficient() > 100) {
            result.rejectValue("coefficient", "error.coefficient", "Coefficient must be between 0 and 100.");
        }

        // Calculate the sum of existing coefficients
        double totalCoefficient = element.getEvaluations().stream()
                .mapToDouble(Evaluation::getCoefficient)
                .sum();

        if (totalCoefficient + evaluation.getCoefficient() > 100) {
            result.rejectValue("coefficient", "error.coefficientSum",
                    "The sum of coefficients for all evaluations in this module must not exceed 100.");
        }

        if (result.hasErrors()) {
            List<Evaluation> evaluations = element.getEvaluations();
            model.addAttribute("moduleElement", element);
            model.addAttribute("evaluations", evaluations);
            return "Dashboard/admin/elementEvaluations";
        }

        // Save the evaluation
        evaluation.setModuleElement(element);
        evaluationRepo.save(evaluation);

        // Add success message
        redirectAttributes.addFlashAttribute("successMessage", "Evaluation added successfully!");
        return "redirect:/Dashboard/admin/elementEvaluations/" + elementId;
    }

    @PostMapping("/deleteEvaluation/{evaluationId}")
    public String deleteEvaluation(
            @PathVariable Long evaluationId,
            RedirectAttributes redirectAttributes
    ) {
        // Find the evaluation by ID
        Evaluation evaluation = evaluationRepo.findById(evaluationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid evaluation ID"));

        // Get the associated element ID for redirection
        Long elementId = evaluation.getModuleElement().getId();

        // Delete the evaluation
        evaluationRepo.delete(evaluation);

        // Add success message
        redirectAttributes.addFlashAttribute("successMessage", "Evaluation deleted successfully!");

        // Redirect to the evaluations page for the element
        return "redirect:/Dashboard/admin/elementEvaluations/" + elementId;
    }

}
