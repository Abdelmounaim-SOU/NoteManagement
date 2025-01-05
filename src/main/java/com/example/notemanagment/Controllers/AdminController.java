package com.example.notemanagment.Controllers;

import com.example.notemanagment.Models.*;
import com.example.notemanagment.Models.Module;
import com.example.notemanagment.Repository.FieldRepo;
import com.example.notemanagment.Repository.ModuleRepo;
import com.example.notemanagment.Repository.ProfRepo;
import com.example.notemanagment.Repository.UserRepo;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Dashboard/admin")
public class AdminController {
    @Autowired
    private HttpSession session;
    @Autowired
    private UserRepo userrepo ;


    @GetMapping({"","/"})
    public String showAdminDashboard(Model model) {
        int userId =  (int) session.getAttribute("userId");
        model.addAttribute("userId", userId);
        var users = userrepo.findAll(Sort.by(Sort.Direction.ASC,"id"));
        model.addAttribute("users",users);
        return "Dashboard/admin/index";
    }




}
