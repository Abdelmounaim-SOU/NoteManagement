package com.example.notemanagment.Controllers;

import com.example.notemanagment.Models.*;
import com.example.notemanagment.Models.Module;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private StudentRepo studentRepo;
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
    @GetMapping("/moduleStudents/{moduleId}")
    public String showModuleStudents(@PathVariable Integer moduleId, Model model) {
        // Find the module by ID
        Module module = moduleRepo.findById(moduleId).orElseThrow(() -> new RuntimeException("Module not found"));

        // Get field and semester of the module
        Field field = module.getField();
        Semester semester = module.getSemester();

        // Find students in the same field and semester
        List<Student> students = studentRepo.findByFieldAndSemester(field, semester);

        // Calculate the average note for each student
        Map<Student, Double> studentAverages = new HashMap<>();
        for (Student student : students) {
            double totalWeightedNote = 0.0;
            double totalCoefficient = 0.0;

            for (ModuleElement element : module.getModuleElements()) {
                double elementTotalWeightedNote = 0.0;
                double elementTotalCoefficient = 0.0;

                // Iterate through evaluations in the module element
                for (Evaluation evaluation : element.getEvaluations()) {
                    // Get the student's note for the evaluation
                    Note note = student.getNotes().stream()
                            .filter(n -> n.getEvaluation().getId().equals(evaluation.getId()))
                            .findFirst().orElse(null);

                    if (note != null) {
                        elementTotalWeightedNote += note.getValue() * (evaluation.getCoefficient() / 100.0);
                        elementTotalCoefficient += evaluation.getCoefficient();
                    }
                }

                // Add the element's contribution to the total
                if (elementTotalCoefficient > 0) {
                    totalWeightedNote += elementTotalWeightedNote;
                    totalCoefficient += elementTotalCoefficient;
                }
            }

            // Calculate the final average for the student
            double averageNote = totalCoefficient > 0 ? totalWeightedNote / (totalCoefficient / 100.0) : 0.0;
            studentAverages.put(student, averageNote);
        }

        // Add data to the model
        model.addAttribute("module", module);
        model.addAttribute("studentAverages", studentAverages);

        return "Dashboard/admin/moduleStudents"; // Ensure this view exists
    }
}
