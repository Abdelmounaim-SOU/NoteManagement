package com.example.notemanagment.Controllers;



import com.example.notemanagment.Models.Field;
import com.example.notemanagment.Models.Semester;
import com.example.notemanagment.Models.Student;
import com.example.notemanagment.Repository.FieldRepo;
import com.example.notemanagment.Repository.SemesterRepo;
import com.example.notemanagment.Repository.StudentRepo;
import com.example.notemanagment.Services.FieldService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/Dashboard/admin")
public class StudentController {
@Autowired
private StudentRepo studentRepo ;
@Autowired
private FieldRepo fieldRepo;
@Autowired
private SemesterRepo semesterRepo ;
@GetMapping("/fieldStudents/{fieldId}/{semesterId}")
public String getFieldStudents(
@PathVariable Integer fieldId,
@PathVariable Long semesterId,
        Model model) {
        var students = studentRepo.findByFieldIdAndSemesterId(fieldId, semesterId);
        model.addAttribute("students", students);
        return "Dashboard/admin/fieldStudents";
        }
    @GetMapping("/fieldStudents/{fieldId}/{semesterId}/addStudent")
    public String showAddStudentForm(
            @PathVariable Integer fieldId,
            @PathVariable Long semesterId,
            Model model) {
        Student student = new Student();
        model.addAttribute("student", student);
        model.addAttribute("fieldId", fieldId);
        model.addAttribute("semesterId", semesterId);
        return "Dashboard/admin/addStudent";
    }

    @PostMapping("/fieldStudents/{fieldId}/{semesterId}/addStudent")
    public String addStudentToFieldAndSemester(
            @PathVariable Integer fieldId,
            @PathVariable Long semesterId,
            @ModelAttribute @Valid Student student,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "Dashboard/admin/addStudent";
        }
        Field field = fieldRepo.findById(fieldId).orElse(null);
        Semester semester = semesterRepo.findById(Math.toIntExact(semesterId)).orElse(null);
        if (field == null || semester == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid field or semester.");
            return "redirect:/Dashboard/admin/fieldStudents/" + fieldId + "/" + semesterId;
        }
        student.setField(field);
        student.setSemester(semester);
        studentRepo.save(student);
        redirectAttributes.addFlashAttribute("success", "Student added successfully.");
        return "redirect:/Dashboard/admin/fieldStudents/" + fieldId + "/" + semesterId;
    }
}
