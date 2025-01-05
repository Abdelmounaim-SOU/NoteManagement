package com.example.notemanagment.Controllers;

import com.example.notemanagment.Models.User;
import com.example.notemanagment.Models.UserDto;
import com.example.notemanagment.Repository.UserRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/Dashboard/admin")
public class UsersController {
    @Autowired
    private UserRepo userrepo ;
    @GetMapping({"/createuser"})
    public String createUser(Model model){
        UserDto userDto = new UserDto();
        model.addAttribute("userDto",userDto);
        return "Dashboard/admin/createuser";
    }
    @PostMapping("/createuser")
    public String createClient(@Valid @ModelAttribute UserDto userDto, BindingResult result){
        if(userrepo.findByUsername(userDto.getUsername())!=null){
            result.addError(
                    new FieldError("userDto","username",userDto.getUsername(),false,null,null,"username is already used ")
            );
        }
        if (!("admin".equals(userDto.getRole())|| "prof".equals(userDto.getRole()))) {
            result.addError(
                    new FieldError("userDto","role",userDto.getRole(),false,null,null,"role can either be admin or prof ")
            );
        }
        if(result.hasErrors()){
            return "Dashboard/admin/createuser";
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        userrepo.save(user);
        return "redirect:/Dashboard/admin";
    }

    @GetMapping("/edit")
    public String editUser(Model model, @RequestParam int id) {
        User user = userrepo.findById(id).orElse(null);
        if (user == null) {
            return "redirect:/Dashboard/admin";
        }
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());
        model.addAttribute("userDto", userDto);
        model.addAttribute("user", user); // Pass the user ID to the model for form submission
        return "Dashboard/admin/edit"; // Return the template name
    }

    @PostMapping("/edit")
    public String editUser(
            @RequestParam int id,
            @Valid @ModelAttribute UserDto userDto,
            BindingResult result,
            Model model) {
        User user = userrepo.findById(id).orElse(null);
        if (user == null) {
            return "redirect:/Dashboard/admin";
        }
        model.addAttribute("userId", id); // Keep the ID for reuse in the form

        if (result.hasErrors()) {
            return "Dashboard/admin/edit"; // Return to the edit page with errors
        }

        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());

        try {
            userrepo.save(user);
        } catch (Exception ex) {
            result.addError(new FieldError(
                    "userDto", "username", userDto.getUsername(), false, null, null, "Username already used"));
            return "Dashboard/admin/edit"; // Return to the edit page with error
        }

        return "redirect:/Dashboard/admin";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam int id){
        User user = userrepo.findById(id).orElse(null);
        if(user!=null){
            userrepo.delete(user);
        }
        return "redirect:/Dashboard/admin";
    }
}
