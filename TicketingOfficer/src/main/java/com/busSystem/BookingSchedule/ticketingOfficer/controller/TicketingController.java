package com.busSystem.BookingSchedule.ticketingOfficer.controller;

import com.busSystem.BookingSchedule.ticketingOfficer.model.Ticket;
import com.busSystem.BookingSchedule.ticketingOfficer.model.Fare;
import com.busSystem.BookingSchedule.ticketingOfficer.service.TicketService;
import com.busSystem.BookingSchedule.ticketingOfficer.service.FareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/ticketing")
public class TicketingController {
    
    @Autowired
    private TicketService ticketService;
    
    @Autowired
    private FareService fareService;
    
    @GetMapping("")
    public String ticketingDashboard(HttpSession session, Model model) {
        if (session.getAttribute("userRole") == null || 
            !session.getAttribute("userRole").equals("TICKETING_OFFICER")) {
            return "redirect:/login";
        }
        
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("totalTickets", ticketService.getAllTickets().size());
        model.addAttribute("totalFares", fareService.getAllFares().size());
        
        return "ticketing/dashboard";
    }
    
    // Ticket Management
    @GetMapping("/tickets")
    public String listTickets(Model model, HttpSession session) {
        if (session.getAttribute("userRole") == null || 
            !session.getAttribute("userRole").equals("TICKETING_OFFICER")) {
            return "redirect:/login";
        }
        
        model.addAttribute("tickets", ticketService.getAllTickets());
        return "ticketing/tickets/list";
    }
    
    @GetMapping("/tickets/new")
    public String newTicketForm(Model model, HttpSession session) {
        if (session.getAttribute("userRole") == null || 
            !session.getAttribute("userRole").equals("TICKETING_OFFICER")) {
            return "redirect:/login";
        }
        
        model.addAttribute("ticket", new Ticket());
        return "ticketing/tickets/form";
    }
    
    @PostMapping("/tickets")
    public String saveTicket(@ModelAttribute Ticket ticket, HttpSession session) {
        if (session.getAttribute("userRole") == null || 
            !session.getAttribute("userRole").equals("TICKETING_OFFICER")) {
            return "redirect:/login";
        }
        
        ticketService.saveTicket(ticket);
        return "redirect:/ticketing/tickets";
    }
    
    @GetMapping("/tickets/edit/{id}")
    public String editTicketForm(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("userRole") == null || 
            !session.getAttribute("userRole").equals("TICKETING_OFFICER")) {
            return "redirect:/login";
        }
        
        model.addAttribute("ticket", ticketService.getTicketById(id).orElse(new Ticket()));
        return "ticketing/tickets/form";
    }
    
    @GetMapping("/tickets/delete/{id}")
    public String deleteTicket(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("userRole") == null || 
            !session.getAttribute("userRole").equals("TICKETING_OFFICER")) {
            return "redirect:/login";
        }
        
        ticketService.deleteTicket(id);
        return "redirect:/ticketing/tickets";
    }
    
    // Fare Management
    @GetMapping("/fares")
    public String listFares(Model model, HttpSession session) {
        if (session.getAttribute("userRole") == null || 
            !session.getAttribute("userRole").equals("TICKETING_OFFICER")) {
            return "redirect:/login";
        }
        
        model.addAttribute("fares", fareService.getAllFares());
        return "ticketing/fares/list";
    }
    
    @GetMapping("/fares/new")
    public String newFareForm(Model model, HttpSession session) {
        if (session.getAttribute("userRole") == null || 
            !session.getAttribute("userRole").equals("TICKETING_OFFICER")) {
            return "redirect:/login";
        }
        
        model.addAttribute("fare", new Fare());
        return "ticketing/fares/form";
    }
    
    @PostMapping("/fares")
    public String saveFare(@ModelAttribute Fare fare, HttpSession session) {
        if (session.getAttribute("userRole") == null || 
            !session.getAttribute("userRole").equals("TICKETING_OFFICER")) {
            return "redirect:/login";
        }
        
        fareService.saveFare(fare);
        return "redirect:/ticketing/fares";
    }
    
    @GetMapping("/fares/edit/{id}")
    public String editFareForm(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("userRole") == null || 
            !session.getAttribute("userRole").equals("TICKETING_OFFICER")) {
            return "redirect:/login";
        }
        
        model.addAttribute("fare", fareService.getFareById(id).orElse(new Fare()));
        return "ticketing/fares/form";
    }
    
    @GetMapping("/fares/delete/{id}")
    public String deleteFare(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("userRole") == null || 
            !session.getAttribute("userRole").equals("TICKETING_OFFICER")) {
            return "redirect:/login";
        }
        
        fareService.deleteFare(id);
        return "redirect:/ticketing/fares";
    }
}