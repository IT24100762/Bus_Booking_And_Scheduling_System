package com.busSystem.BookingSchedule.itSupport.controller;

import com.busSystem.BookingSchedule.itSupport.model.User;
import com.busSystem.BookingSchedule.itSupport.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/itsupport")
public class ItSupportController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("IT_SUPPORT")) {
            return "redirect:/login";
        }
        return "itSupport/dashboard";
    }
}