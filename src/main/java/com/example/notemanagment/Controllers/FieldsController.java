package com.example.notemanagment.Controllers;

import com.example.notemanagment.Models.Field;
import com.example.notemanagment.Models.FieldDto;
import com.example.notemanagment.Models.Module;
import com.example.notemanagment.Models.ModuleDto;
import com.example.notemanagment.Repository.*;
import com.example.notemanagment.Services.FieldService;
import com.example.notemanagment.Services.ModuleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/Dashboard/admin")
public class FieldsController {
    @Autowired
    private FieldRepo fieldRepo;
    @Autowired
    private UserRepo userrepo ;
    @Autowired
    private FieldService fieldService;
    @Autowired
    private ModuleRepo moduleRepo ;
    @Autowired
    private SemesterRepo semesterRepo;

    @GetMapping({"/Managefields"})
    public String ShowFiedls(Model model) {
        var fields = fieldRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
        var semesters = semesterRepo.findAll(Sort.by(Sort.Direction.ASC, "id")); // Fetch semesters
        model.addAttribute("fields", fields);
        model.addAttribute("semesters", semesters);
        return "Dashboard/admin/Managefields";
    }



    @GetMapping({"/createfield"})
    public String createField(Model model){
        FieldDto fieldDto = new FieldDto();
        model.addAttribute("fieldDto",fieldDto);
        return "Dashboard/admin/createfield";
    }
    @PostMapping("/createfield")
    public String createField(@Valid @ModelAttribute FieldDto fieldDto, BindingResult result){
        if(userrepo.findByUsername(fieldDto.getName())!=null){
            result.addError(
                    new FieldError("fieldDto","name",fieldDto.getName(),false,null,null,"name is already used ")
            );
        }
        if(result.hasErrors()){
            return "Dashboard/admin/createfield";
        }
        Field field = new Field();
        field.setName(fieldDto.getName());
        fieldRepo.save(field);
        return "redirect:/Dashboard/admin/Managefields";
    }
    @GetMapping("/deletefield")
    public String deletefield(@RequestParam int id){
        Field field = fieldRepo.findById(id).orElse(null);
        if(field!=null){
            fieldRepo.delete(field);
        }
        return "redirect:/Dashboard/admin/Managefields";
    }
    @GetMapping("/fieldModules/{fieldId}")
    public String getFieldModules(@PathVariable Integer fieldId, Model model) {
        Field field = fieldService.findById(fieldId);
        model.addAttribute("field", field);
        model.addAttribute("modules", field.getModules());
        return "Dashboard/admin/fieldModules"; // Create this view
    }



}
