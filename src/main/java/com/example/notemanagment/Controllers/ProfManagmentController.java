package com.example.notemanagment.Controllers;

import com.example.notemanagment.Models.Professor;
import com.example.notemanagment.Models.ProfessorDto;
import com.example.notemanagment.Models.User;
import com.example.notemanagment.Repository.ProfRepo;
import com.example.notemanagment.Repository.UserRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/Dashboard/admin")
public class ProfManagmentController {
    @Autowired
    private ProfRepo profRepo;
    @Autowired
    private UserRepo userrepo ;
    @GetMapping({"/ManageProfs"})
    public String ShowProfs(Model model) {
        var profs = profRepo.findAll(Sort.by(Sort.Direction.ASC,"id"));
        model.addAttribute("profs",profs);
        return "Dashboard/admin/ManageProfs";
    }

    @GetMapping({"/AddProf"})
    public String AddProf(Model model, @RequestParam int id) {
        User user = userrepo.findById(id).orElse(null);
        if (user == null) {
            return "redirect:/Dashboard/admin";
        }
        ProfessorDto profDto = new ProfessorDto();
        String generatedCode = "prof_" + id; // Generate the code
        profDto.setCode(generatedCode); // Set the generated code in the DTO
        model.addAttribute("profDto", profDto);
        model.addAttribute("user", user);
        return "Dashboard/admin/AddProf";
    }

    @PostMapping("/AddProf")
    public String Addprof(
            @RequestParam int id,
            @Valid @ModelAttribute ProfessorDto profDto,
            BindingResult result,
            Model model) {
        // Check if the User exists
        User user = userrepo.findById(id).orElse(null);
        if (user == null) {
            return "redirect:/Dashboard/admin";
        }

        // Check if a Professor already exists for this ID
        Professor prof = profRepo.findById(id).orElse(new Professor());
        prof.setId(id); // Set the professor ID
        prof.setName(profDto.getName());
        prof.setSurname(profDto.getSurname());
        prof.setCode(profDto.getCode());
        prof.setSpecialty(profDto.getSpecialty());

        try {
            profRepo.save(prof); // Save the professor
        } catch (Exception ex) {
            result.addError(new FieldError(
                    "profDto", "code", profDto.getCode(), false, null, null, "Code already used"));
            model.addAttribute("user", user); // Pass the user back to the form
            return "Dashboard/admin/AddProf"; // Return to the form with errors
        }

        return "redirect:/Dashboard/admin";
    }
    @GetMapping("/deleteprof")
    public String deleteProf(@RequestParam int id){
        Professor prof = profRepo.findById(id).orElse(null);
        if(prof!=null){
            profRepo.delete(prof);
        }
        return "redirect:/Dashboard/admin/ManageProfs";
    }
}