package com.example.notemanagment.Controllers;

import com.example.notemanagment.Models.Field;
import com.example.notemanagment.Models.Module;
import com.example.notemanagment.Models.ModuleDto;
import com.example.notemanagment.Models.Semester;
import com.example.notemanagment.Repository.FieldRepo;
import com.example.notemanagment.Repository.ModuleRepo;
import com.example.notemanagment.Repository.SemesterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/Dashboard/admin")
public class ModulesController {

    @Autowired
    private FieldRepo fieldRepo;

    @Autowired
    private ModuleRepo moduleRepo;

    @Autowired
    private SemesterRepo semesterRepo;

    @GetMapping("/createModule/{fieldId}")
    public String createModule(@PathVariable Integer fieldId, Model model) {
        ModuleDto moduleDto = new ModuleDto();
        moduleDto.setFieldId(fieldId); // Pre-set the fieldId in the DTO
        model.addAttribute("moduleDto", moduleDto);

        // Fetch all semesters to populate the dropdown
        List<Semester> semesters = semesterRepo.findAll();
        model.addAttribute("semesters", semesters);

        return "Dashboard/admin/createModule";
    }

    @PostMapping("/createModule/{fieldId}")
    public String saveModule(
            @PathVariable Integer fieldId,
            @ModelAttribute("moduleDto") ModuleDto moduleDto,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        // Validate semester
        Semester selectedSemester = semesterRepo.findById(Math.toIntExact(moduleDto.getSemesterId()))
                .orElse(null);
        if (selectedSemester == null) {
            result.rejectValue("semesterId", "Invalid.semester", "Invalid semester selected.");
            return "Dashboard/admin/createModule";
        }

        if (result.hasErrors()) {
            return "Dashboard/admin/createModule";
        }

        // Fetch the field by fieldId
        Field selectedField = fieldRepo.findById(fieldId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid field ID"));

        // Map DTO to Module entity
        Module module = new Module();
        module.setName(moduleDto.getName());
        module.setCode(moduleDto.getCode());
        module.setSemester(selectedSemester); // Link the module to the semester
        module.setField(selectedField); // Link the module to the field

        // Save the module
        moduleRepo.save(module);

        // Add success message
        redirectAttributes.addFlashAttribute("successMessage", "Module created successfully!");
        return "redirect:/Dashboard/admin/Managefields";
    }

    @PostMapping("/deleteModule/{fieldId}/{moduleId}")
    public String deleteModule(
            @PathVariable Integer fieldId,
            @PathVariable Long moduleId,
            RedirectAttributes redirectAttributes) {
        // Delete the module by its ID
        moduleRepo.deleteById(Math.toIntExact(moduleId));

        // Add a success message
        redirectAttributes.addFlashAttribute("successMessage", "Module deleted successfully!");

        // Redirect to the fieldModules page for the specific field
        return "redirect:/Dashboard/admin/fieldModules/" + fieldId;
    }
}
