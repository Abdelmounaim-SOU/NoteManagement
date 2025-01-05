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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/Dashboard/prof")
public class StudentListController {

    @Autowired
    private StudentRepo studentRepository;

    @Autowired
    private HttpSession session;

    @Autowired
    private ModuleElementRepository moduleElementRepo;

    @Autowired
    private ProfRepo profRepo;

    @Autowired
    private StudentRepo studentRepo;

    @GetMapping("listEtudiants/{elementId}") // listEtudiants/{profId}/students/{semesterId}/{fieldId}
    public String getStudentsBySemesterAndField(@PathVariable("elementId") Long elementId, Model model) {

        System.out.println("Successfull retrieved the Element!");
        // Fetch the ModuleElement
        Optional<ModuleElement> moduleElementOptional = moduleElementRepo.findById(elementId);
        if (moduleElementOptional.isEmpty()) {
            System.out.println("Error: Module element not found for ID " + elementId);
            return "error"; // Redirect to an error page if not found
        } else {
            System.out.println("Successfull retrieved the Element!");
        }

        // Get the element (maybe!)
        ModuleElement moduleElement = moduleElementOptional.get();

        // Fetch the associated Module and Semester
        Module module = moduleElement.getModule();

        // Get semester for some shit or some.
        Semester semester = module.getSemester();

        // get the field ID (what a fucken name FIELD!)
        Field field = module.getField();

        // Fetch the professor details (if needed, via session or another method)
        Integer profId = (Integer) session.getAttribute("userId");

        // Check for null id of the professor:
        if (profId == null) {
            System.out.println("Error: Professor ID not found in session.");
            return "error";
        }

        // for debuggin L
        System.out.println("Started to retrieve students : ");

        // Fetch associated students
        var students = studentRepo.findBySemester_IdAndField_Id(semester.getId(), (long) field.getId());

        if (students.isEmpty()) {
            System.out.println("Leaks a.out");
        } else {
            System.out.println("khdaaaaaaam!");
        }

        // Add data to the model
        model.addAttribute("moduleElement", moduleElement);
        model.addAttribute("semester", semester);
        model.addAttribute("field", field);
        model.addAttribute("profId", profId);
        model.addAttribute("students", students);

        return "Dashboard/prof/listEtudiants"; // Name of the HTML view file
    }
}
// Path of listEtudiants : src/main/resources/templates/Dashboard/prof/listEtudiants.html

