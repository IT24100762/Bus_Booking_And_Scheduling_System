package com.busSystem.BookingSchedule.passenger.controller;

import com.busSystem.BookingSchedule.passenger.model.Booking;
import com.busSystem.BookingSchedule.passenger.model.PassengerProfile;
import com.busSystem.BookingSchedule.passenger.model.Complaint;
import com.busSystem.BookingSchedule.passenger.model.Feedback;
import com.busSystem.BookingSchedule.passenger.service.BookingService;
import com.busSystem.BookingSchedule.passenger.service.PassengerProfileService;
import com.busSystem.BookingSchedule.passenger.service.ComplaintService;
import com.busSystem.BookingSchedule.passenger.service.FeedbackService;
import com.busSystem.BookingSchedule.operationsManager.model.Route;
import com.busSystem.BookingSchedule.operationsManager.model.Schedule;
import com.busSystem.BookingSchedule.operationsManager.service.RouteService;
import com.busSystem.BookingSchedule.operationsManager.service.ScheduleService;
import com.busSystem.BookingSchedule.ticketingOfficer.model.Fare;
import com.busSystem.BookingSchedule.ticketingOfficer.model.Ticket;
import com.busSystem.BookingSchedule.ticketingOfficer.service.FareService;
import com.busSystem.BookingSchedule.ticketingOfficer.service.TicketService;
import com.busSystem.BookingSchedule.itSupport.model.User;
import com.busSystem.BookingSchedule.itSupport.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/passenger")
public class PassengerController {
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private PassengerProfileService passengerProfileService;
    
    @Autowired
    private ComplaintService complaintService;
    
    @Autowired
    private FeedbackService feedbackService;
    
    @Autowired
    private RouteService routeService;
    
    @Autowired
    private ScheduleService scheduleService;
    
    @Autowired
    private FareService fareService;
    
    @Autowired
    private TicketService ticketService;
    
    @Autowired
    private UserService userService;
    
    // Dashboard
    @GetMapping("")
    public String dashboard(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        List<Booking> recentBookings = bookingService.getBookingsByPassenger(userId);
        model.addAttribute("bookings", recentBookings.size() > 5 ? recentBookings.subList(0, 5) : recentBookings);
        model.addAttribute("userName", session.getAttribute("userName"));
        return "passenger/dashboard";
    }
    
    // Registration
    @GetMapping("/register")
    public String registerForm() {
        return "passenger/register";
    }
    
    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String password,
                          @RequestParam String firstName, @RequestParam String lastName,
                          @RequestParam String phoneNumber, Model model) {
        Optional<User> existingUser = userService.getUserByEmail(email);
        if (existingUser.isPresent()) {
            model.addAttribute("error", "Email already exists");
            return "passenger/register";
        }
        
        User user = new User(email, password, firstName, lastName, "PASSENGER", "ACTIVE", phoneNumber);
        userService.saveUser(user);
        model.addAttribute("success", "Registration successful. Please login.");
        return "login";
    }
    
    // Booking Management
    @GetMapping("/bookings")
    public String viewBookings(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        List<Booking> bookings = bookingService.getBookingsByPassenger(userId);
        model.addAttribute("bookings", bookings);
        return "passenger/bookings";
    }
    
    @GetMapping("/bookings/new")
    public String newBookingForm(Model model) {
        List<Route> routes = routeService.getRoutesByStatus("ACTIVE");
        model.addAttribute("routes", routes);
        return "passenger/booking-form";
    }
    
    @GetMapping("/routes/{routeId}/schedules")
    @ResponseBody
    public List<Schedule> getSchedulesByRoute(@PathVariable Long routeId) {
        return scheduleService.getSchedulesByRouteId(routeId);
    }
    
    @GetMapping("/routes/{routeId}/fares")
    @ResponseBody
    public List<Fare> getFaresByRoute(@PathVariable Long routeId) {
        return fareService.getFaresByRoute(routeId);
    }
    
    @PostMapping("/bookings")
    public String createBooking(@RequestParam Long routeId, @RequestParam Long scheduleId,
                               @RequestParam String ticketType, @RequestParam String travelDate,
                               @RequestParam String seatNumber, @RequestParam(required = false) String specialRequests,
                               HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        // Get fare for the route and ticket type
        List<Fare> fares = fareService.getActiveFaresByRouteAndType(routeId, ticketType);
        if (fares.isEmpty()) {
            model.addAttribute("error", "No fare found for selected route and ticket type");
            return "passenger/booking-form";
        }
        
        Fare fare = fares.get(0);
        Double fareAmount = fare.getBasePrice();
        Double discountApplied = fareAmount * (fare.getDiscountPercentage() / 100);
        Double finalAmount = fareAmount - discountApplied;
        
        LocalDateTime travelDateTime = LocalDateTime.parse(travelDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        
        Booking booking = new Booking(userId, routeId, scheduleId, ticketType, fareAmount, 
                                    discountApplied, finalAmount, travelDateTime, seatNumber, "BOOKED");
        booking.setSpecialRequests(specialRequests);
        
        bookingService.saveBooking(booking);
        return "redirect:/passenger/bookings";
    }
    
    @GetMapping("/bookings/{id}/edit")
    public String editBookingForm(@PathVariable Long id, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        Optional<Booking> bookingOpt = bookingService.getBookingById(id);
        if (bookingOpt.isEmpty() || !bookingOpt.get().getPassengerId().equals(userId)) {
            return "redirect:/passenger/bookings";
        }
        
        Booking booking = bookingOpt.get();
        List<Route> routes = routeService.getRoutesByStatus("ACTIVE");
        List<Schedule> schedules = scheduleService.getSchedulesByRouteId(booking.getRouteId());
        
        model.addAttribute("booking", booking);
        model.addAttribute("routes", routes);
        model.addAttribute("schedules", schedules);
        return "passenger/booking-edit";
    }
    
    @PostMapping("/bookings/{id}")
    public String updateBooking(@PathVariable Long id, @RequestParam String travelDate,
                               @RequestParam String seatNumber, @RequestParam(required = false) String specialRequests,
                               HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        Optional<Booking> bookingOpt = bookingService.getBookingById(id);
        if (bookingOpt.isEmpty() || !bookingOpt.get().getPassengerId().equals(userId)) {
            return "redirect:/passenger/bookings";
        }
        
        Booking booking = bookingOpt.get();
        booking.setTravelDate(LocalDateTime.parse(travelDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        booking.setSeatNumber(seatNumber);
        booking.setSpecialRequests(specialRequests);
        
        bookingService.saveBooking(booking);
        return "redirect:/passenger/bookings";
    }
    
    @PostMapping("/bookings/{id}/cancel")
    public String cancelBooking(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        Optional<Booking> bookingOpt = bookingService.getBookingById(id);
        if (bookingOpt.isEmpty() || !bookingOpt.get().getPassengerId().equals(userId)) {
            return "redirect:/passenger/bookings";
        }
        
        Booking booking = bookingOpt.get();
        booking.setStatus("CANCELLED");
        bookingService.saveBooking(booking);
        return "redirect:/passenger/bookings";
    }
    
    // Profile Management
    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        Optional<User> user = userService.getUserById(userId);
        Optional<PassengerProfile> profile = passengerProfileService.getProfileByUserId(userId);
        
        model.addAttribute("user", user.orElse(null));
        model.addAttribute("profile", profile.orElse(null));
        return "passenger/profile";
    }
    
    @GetMapping("/profile/edit")
    public String editProfileForm(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        Optional<User> user = userService.getUserById(userId);
        Optional<PassengerProfile> profile = passengerProfileService.getProfileByUserId(userId);
        
        model.addAttribute("user", user.orElse(null));
        model.addAttribute("profile", profile.orElse(new PassengerProfile()));
        return "passenger/profile-edit";
    }
    
    @PostMapping("/profile")
    public String updateProfile(@RequestParam String firstName, @RequestParam String lastName,
                               @RequestParam String phoneNumber, @RequestParam(required = false) String preferredSeatType,
                               @RequestParam(required = false) String frequentDestinations, @RequestParam(required = false) String paymentMethods,
                               @RequestParam(required = false) String accessibilityNeeds, @RequestParam(required = false) String travelPreferences,
                               @RequestParam(required = false) String emergencyContactName, @RequestParam(required = false) String emergencyContactPhone,
                               HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        // Update user basic info
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhoneNumber(phoneNumber);
            userService.saveUser(user);
            session.setAttribute("userName", firstName + " " + lastName);
        }
        
        // Update or create profile
        Optional<PassengerProfile> profileOpt = passengerProfileService.getProfileByUserId(userId);
        PassengerProfile profile;
        if (profileOpt.isPresent()) {
            profile = profileOpt.get();
        } else {
            profile = new PassengerProfile();
            profile.setUserId(userId);
            profile.setStatus("ACTIVE");
        }
        
        profile.setPreferredSeatType(preferredSeatType);
        profile.setFrequentDestinations(frequentDestinations);
        profile.setPaymentMethods(paymentMethods);
        profile.setAccessibilityNeeds(accessibilityNeeds);
        profile.setTravelPreferences(travelPreferences);
        profile.setEmergencyContactName(emergencyContactName);
        profile.setEmergencyContactPhone(emergencyContactPhone);
        
        passengerProfileService.saveProfile(profile);
        return "redirect:/passenger/profile";
    }
    
    // Search Routes
    @GetMapping("/routes")
    public String searchRoutes(@RequestParam(required = false) String search, Model model) {
        List<Route> routes;
        if (search != null && !search.trim().isEmpty()) {
            routes = routeService.searchRoutes(search);
        } else {
            routes = routeService.getRoutesByStatus("ACTIVE");
        }
        model.addAttribute("routes", routes);
        model.addAttribute("search", search);
        return "passenger/routes";
    }
    
    @GetMapping("/routes/{id}/details")
    public String routeDetails(@PathVariable Long id, Model model) {
        Optional<Route> route = routeService.getRouteById(id);
        if (route.isEmpty()) {
            return "redirect:/passenger/routes";
        }
        
        List<Schedule> schedules = scheduleService.getSchedulesByRouteId(id);
        List<Fare> fares = fareService.getFaresByRoute(id);
        
        model.addAttribute("route", route.get());
        model.addAttribute("schedules", schedules);
        model.addAttribute("fares", fares);
        return "passenger/route-details";
    }
    
    // Complaint Management
    @GetMapping("/complaints")
    public String viewComplaints(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        List<Complaint> complaints = complaintService.getComplaintsByPassenger(userId);
        model.addAttribute("complaints", complaints);
        return "passenger/complaints";
    }
    
    @GetMapping("/complaints/new")
    public String newComplaintForm(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        List<Ticket> tickets = ticketService.getTicketsByPassenger(userId);
        model.addAttribute("tickets", tickets);
        return "passenger/complaint-form";
    }
    
    @PostMapping("/complaints")
    public String createComplaint(@RequestParam(required = false) Long ticketId, @RequestParam String subject,
                                 @RequestParam String description, @RequestParam String category,
                                 @RequestParam String priority, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        Complaint complaint = new Complaint(userId, ticketId, subject, description, category, priority, "PENDING");
        complaintService.saveComplaint(complaint);
        return "redirect:/passenger/complaints";
    }
    
    @GetMapping("/complaints/{id}/edit")
    public String editComplaintForm(@PathVariable Long id, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        Optional<Complaint> complaintOpt = complaintService.getComplaintById(id);
        if (complaintOpt.isEmpty() || !complaintOpt.get().getPassengerId().equals(userId)) {
            return "redirect:/passenger/complaints";
        }
        
        List<Ticket> tickets = ticketService.getTicketsByPassenger(userId);
        model.addAttribute("complaint", complaintOpt.get());
        model.addAttribute("tickets", tickets);
        return "passenger/complaint-edit";
    }
    
    @PostMapping("/complaints/{id}")
    public String updateComplaint(@PathVariable Long id, @RequestParam(required = false) Long ticketId,
                                 @RequestParam String subject, @RequestParam String description,
                                 @RequestParam String category, @RequestParam String priority,
                                 HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        Optional<Complaint> complaintOpt = complaintService.getComplaintById(id);
        if (complaintOpt.isEmpty() || !complaintOpt.get().getPassengerId().equals(userId)) {
            return "redirect:/passenger/complaints";
        }
        
        Complaint complaint = complaintOpt.get();
        complaint.setTicketId(ticketId);
        complaint.setSubject(subject);
        complaint.setDescription(description);
        complaint.setCategory(category);
        complaint.setPriority(priority);
        
        complaintService.saveComplaint(complaint);
        return "redirect:/passenger/complaints";
    }
    
    @PostMapping("/complaints/{id}/delete")
    public String deleteComplaint(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        Optional<Complaint> complaintOpt = complaintService.getComplaintById(id);
        if (complaintOpt.isEmpty() || !complaintOpt.get().getPassengerId().equals(userId)) {
            return "redirect:/passenger/complaints";
        }
        
        complaintService.deleteComplaint(id);
        return "redirect:/passenger/complaints";
    }
    
    // Feedback Management
    @GetMapping("/feedbacks")
    public String viewFeedbacks(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        List<Feedback> feedbacks = feedbackService.getFeedbacksByPassenger(userId);
        model.addAttribute("feedbacks", feedbacks);
        return "passenger/feedbacks";
    }
    
    @GetMapping("/feedbacks/new")
    public String newFeedbackForm() {
        return "passenger/feedback-form";
    }
    
    @PostMapping("/feedbacks")
    public String createFeedback(@RequestParam Integer rating, @RequestParam String subject,
                                @RequestParam String message, @RequestParam String category,
                                @RequestParam String serviceType, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        Feedback feedback = new Feedback(userId, rating, subject, message, category, serviceType, "PENDING");
        feedbackService.saveFeedback(feedback);
        return "redirect:/passenger/feedbacks";
    }
    
    @GetMapping("/feedbacks/{id}/edit")
    public String editFeedbackForm(@PathVariable Long id, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        Optional<Feedback> feedbackOpt = feedbackService.getFeedbackById(id);
        if (feedbackOpt.isEmpty() || !feedbackOpt.get().getPassengerId().equals(userId)) {
            return "redirect:/passenger/feedbacks";
        }
        
        model.addAttribute("feedback", feedbackOpt.get());
        return "passenger/feedback-edit";
    }
    
    @PostMapping("/feedbacks/{id}")
    public String updateFeedback(@PathVariable Long id, @RequestParam Integer rating,
                                @RequestParam String subject, @RequestParam String message,
                                @RequestParam String category, @RequestParam String serviceType,
                                HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        Optional<Feedback> feedbackOpt = feedbackService.getFeedbackById(id);
        if (feedbackOpt.isEmpty() || !feedbackOpt.get().getPassengerId().equals(userId)) {
            return "redirect:/passenger/feedbacks";
        }
        
        Feedback feedback = feedbackOpt.get();
        feedback.setRating(rating);
        feedback.setSubject(subject);
        feedback.setMessage(message);
        feedback.setCategory(category);
        feedback.setServiceType(serviceType);
        
        feedbackService.saveFeedback(feedback);
        return "redirect:/passenger/feedbacks";
    }
    
    @PostMapping("/feedbacks/{id}/delete")
    public String deleteFeedback(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        Optional<Feedback> feedbackOpt = feedbackService.getFeedbackById(id);
        if (feedbackOpt.isEmpty() || !feedbackOpt.get().getPassengerId().equals(userId)) {
            return "redirect:/passenger/feedbacks";
        }
        
        feedbackService.deleteFeedback(id);
        return "redirect:/passenger/feedbacks";
    }
}