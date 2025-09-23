package com.busSystem.BookingSchedule.customerService.controller;

import com.busSystem.BookingSchedule.customerService.service.CustomerServiceComplaintService;
import com.busSystem.BookingSchedule.customerService.service.CustomerServiceFeedbackService;
import com.busSystem.BookingSchedule.customerService.model.ComplaintResponse;
import com.busSystem.BookingSchedule.customerService.model.FeedbackResponse;
import com.busSystem.BookingSchedule.passenger.model.Complaint;
import com.busSystem.BookingSchedule.passenger.model.Feedback;
import com.busSystem.BookingSchedule.passenger.model.Booking;
import com.busSystem.BookingSchedule.itSupport.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customersupport")
public class CustomerServiceController {
    
    @Autowired
    private CustomerServiceComplaintService complaintService;
    
    @Autowired
    private CustomerServiceFeedbackService feedbackService;
    
    @GetMapping
    public String dashboard() {
        return "customersupport/dashboard";
    }
    
    // Complaint Management
    @GetMapping("/complaints")
    public String listComplaints(@RequestParam(required = false) String status,
                                @RequestParam(required = false) String category,
                                HttpSession session, Model model) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("CUSTOMER_SUPPORT")) {
            return "redirect:/login";
        }

        List<Complaint> complaints;
        if (status != null && !status.isEmpty()) {
            complaints = complaintService.getComplaintsByStatus(status);
        } else if (category != null && !category.isEmpty()) {
            complaints = complaintService.getComplaintsByCategory(category);
        } else {
            complaints = complaintService.getAllComplaints();
        }
        model.addAttribute("complaints", complaints);
        return "customersupport/complaints";
    }
    
    @GetMapping("/complaints/{id}")
    public String viewComplaint(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("CUSTOMER_SUPPORT")) {
            return "redirect:/login";
        }

        Optional<Complaint> complaint = complaintService.getComplaintById(id);
        if (complaint.isPresent()) {
            model.addAttribute("complaint", complaint.get());
            
            Optional<User> passenger = complaintService.getPassengerByComplaint(id);
            passenger.ifPresent(user -> model.addAttribute("passenger", user));
            
            Optional<Booking> booking = complaintService.getBookingByComplaint(id);
            booking.ifPresent(b -> model.addAttribute("booking", b));
            
            List<ComplaintResponse> responses = complaintService.getResponsesByComplaint(id);
            model.addAttribute("responses", responses);
            
            return "customersupport/complaint-detail";
        }
        return "redirect:/customersupport/complaints";
    }
    
    @PostMapping("/complaints/{id}/respond")
    public String respondToComplaint(@PathVariable Long id,
                                   @RequestParam String responseMessage,
                                   @RequestParam String internalNotes,
                                   @RequestParam String status,
                                   @RequestParam(required = false) String resolution,
                                   HttpSession session) {

        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("CUSTOMER_SUPPORT")) {
            return "redirect:/login";
        }

        Long customerServiceId = (Long) session.getAttribute("userId");
        
        ComplaintResponse response = new ComplaintResponse(id, customerServiceId, 
                                                         responseMessage, internalNotes, "SENT");
        complaintService.saveComplaintResponse(response);
        
        if (resolution != null && !resolution.isEmpty()) {
            complaintService.updateComplaintStatus(id, status, resolution);
        }
        
        return "redirect:/customersupport/complaints/" + id;
    }
    
    // Feedback Management
    @GetMapping("/feedbacks")
    public String listFeedbacks(@RequestParam(required = false) String status,
                               @RequestParam(required = false) String category,
                                HttpSession session, Model model) {

        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("CUSTOMER_SUPPORT")) {
            return "redirect:/login";
        }

        List<Feedback> feedbacks;
        if (status != null && !status.isEmpty()) {
            feedbacks = feedbackService.getFeedbacksByStatus(status);
        } else if (category != null && !category.isEmpty()) {
            feedbacks = feedbackService.getFeedbacksByCategory(category);
        } else {
            feedbacks = feedbackService.getAllFeedbacks();
        }
        model.addAttribute("feedbacks", feedbacks);
        return "customersupport/feedbacks";
    }
    
    @GetMapping("/feedbacks/{id}")
    public String viewFeedback(@PathVariable Long id, HttpSession session, Model model) {

        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("CUSTOMER_SUPPORT")) {
            return "redirect:/login";
        }

        Optional<Feedback> feedback = feedbackService.getFeedbackById(id);
        if (feedback.isPresent()) {
            model.addAttribute("feedback", feedback.get());
            
            Optional<User> passenger = feedbackService.getPassengerByFeedback(id);
            passenger.ifPresent(user -> model.addAttribute("passenger", user));
            
            List<FeedbackResponse> responses = feedbackService.getResponsesByFeedback(id);
            model.addAttribute("responses", responses);
            
            return "customersupport/feedback-detail";
        }
        return "redirect:/customersupport/feedbacks";
    }
    
    @PostMapping("/feedbacks/{id}/respond")
    public String respondToFeedback(@PathVariable Long id,
                                  @RequestParam String responseMessage,
                                  @RequestParam String internalNotes,
                                  @RequestParam String status,
                                  HttpSession session) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("CUSTOMER_SUPPORT")) {
            return "redirect:/login";
        }

        Long customerServiceId = (Long) session.getAttribute("userId");
        
        FeedbackResponse response = new FeedbackResponse(id, customerServiceId, 
                                                       responseMessage, internalNotes, "SENT");
        feedbackService.saveFeedbackResponse(response);
        
        feedbackService.updateFeedbackStatus(id, status);
        
        return "redirect:/customersupport/feedbacks/" + id;
    }
}