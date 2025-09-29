package com.busSystem.BookingSchedule.itSupport.controller;

import com.busSystem.BookingSchedule.itSupport.model.User;
import com.busSystem.BookingSchedule.itSupport.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/itsupport/users")
public class UserManagementController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String listUsers(HttpSession session, Model model, 
                           @RequestParam(required = false) String role,
                           @RequestParam(required = false) String status) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("IT_SUPPORT")) {
            return "redirect:/login";
        }
        
        if (role != null && !role.isEmpty()) {
            model.addAttribute("users", userService.getUsersByRole(role));
        } else if (status != null && !status.isEmpty()) {
            model.addAttribute("users", userService.getUsersByStatus(status));
        } else {
            model.addAttribute("users", userService.getAllUsers());
        }
        
        return "itSupport/users/list";
    }
    
    @GetMapping("/create")
    public String createUserForm(HttpSession session, Model model) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("IT_SUPPORT")) {
            return "redirect:/login";
        }
        model.addAttribute("user", new User());
        return "itSupport/users/create";
    }
    
    @PostMapping("/create")
    public String createUser(HttpSession session, @ModelAttribute User user) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("IT_SUPPORT")) {
            return "redirect:/login";
        }
        userService.saveUser(user);
        return "redirect:/itsupport/users";
    }
    
    @GetMapping("/edit/{id}")
    public String editUserForm(HttpSession session, @PathVariable Long id, Model model) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("IT_SUPPORT")) {
            return "redirect:/login";
        }
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "itSupport/users/edit";
        }
        return "redirect:/itsupport/users";
    }
    
    @PostMapping("/edit/{id}")
    public String editUser(HttpSession session, @PathVariable Long id, @ModelAttribute User user) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("IT_SUPPORT")) {
            return "redirect:/login";
        }
        user.setId(id);
        userService.saveUser(user);
        return "redirect:/itsupport/users";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteUser(HttpSession session, @PathVariable Long id) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("IT_SUPPORT")) {
            return "redirect:/login";
        }
        userService.deleteUser(id);
        return "redirect:/itsupport/users";
    }
    
    @GetMapping("/view/{id}")
    public String viewUser(HttpSession session, @PathVariable Long id, Model model) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("IT_SUPPORT")) {
            return "redirect:/login";
        }
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "itSupport/users/view";
        }
        return "redirect:/itsupport/users";
    }
}