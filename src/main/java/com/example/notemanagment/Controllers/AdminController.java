package com.example.notemanagment.Controllers;

import com.example.notemanagment.Models.*;
import com.example.notemanagment.Repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Dashboard/admin")
public class AdminController {
    @Autowired
    private HttpSession session;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProfRepo profRepo;
    @Autowired
    private FieldRepo fieldRepo;
    @Autowired
    private ModuleRepo moduleRepo;

    @GetMapping({"", "/"})
    public String showAdminDashboard(Model model) {
        int userId = (int) session.getAttribute("userId");
        model.addAttribute("userId", userId);

        // Fetch all users
        var users = userRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("users", users);

        // Fetch counts for the dashboard
        long studentCount = userRepo.count(); // Assuming "student" is a role
        long profCount = profRepo.count();
        long moduleCount = moduleRepo.count();
        long fieldCount = fieldRepo.count();

        // Add counts to the model
        model.addAttribute("studentCount", studentCount);
        model.addAttribute("profCount", profCount);
        model.addAttribute("moduleCount", moduleCount);
        model.addAttribute("fieldCount", fieldCount);

        return "Dashboard/admin/index";
    }
}