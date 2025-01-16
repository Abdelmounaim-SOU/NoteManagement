package com.example.notemanagment.Controllers;

import com.example.notemanagment.Models.Semester;
import com.example.notemanagment.Models.User;
import com.example.notemanagment.Models.UserDto;
import com.example.notemanagment.Repository.SemesterRepo;
import com.example.notemanagment.Repository.UserRepo;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.List;

@Controller
public class Login_Controller {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SemesterRepo semesterRepo;
    @GetMapping({""})
    public String Login(Model model){
        UserDto userDto = new UserDto();
        initializeSemesters();
        model.addAttribute("userDto",userDto);
        return "index";
    }
    @PostMapping
    public String Login(@Valid @ModelAttribute UserDto userDto, BindingResult result, HttpSession session) {
        System.out.println("Attempting login for username: " + userDto.getUsername());

        // Fetch user from database
        User user = userRepo.findByUsername(userDto.getUsername().trim());
        if (user == null) {
            System.out.println("User not found in database.");
            result.addError(
                    new FieldError("userDto", "username", userDto.getUsername(), false, null, null, "Username not found")
            );
            return "index";
        }

        // Check the role and password
        if ("admin".equals(user.getRole()) && userDto.getPassword().equals(user.getPassword())) {
            session.setAttribute("userId", user.getId()); // Store userId in session
            return "redirect:/Dashboard/admin";
        } else if ("prof".equals(user.getRole()) && userDto.getPassword().equals(user.getPassword())) {
            session.setAttribute("userId", user.getId()); // Store userId in session
            return "redirect:/Dashboard/prof/prof";
        } else {
            result.addError(
                    new FieldError("userDto", "role", user.getRole(), false, null, null, "Invalid role")
            );
            return "index";
        }
    }
    private void initializeSemesters() {
        if (semesterRepo.count() == 0) { // Check if the semesters table is empty
            List<Semester> defaultSemesters = Arrays.asList(
                    new Semester(null, "S1"),
                    new Semester(null, "S2"),
                    new Semester(null, "S3"),
                    new Semester(null, "S4"),
                    new Semester(null, "S5")
            );
            semesterRepo.saveAll(defaultSemesters); // Save the default semesters
        }
    }

}
