package com.busSystem.BookingSchedule.driver.controller;

import com.busSystem.BookingSchedule.driver.model.TripLog;
import com.busSystem.BookingSchedule.driver.model.VehicleConditionReport;
import com.busSystem.BookingSchedule.driver.service.TripLogService;
import com.busSystem.BookingSchedule.driver.service.VehicleConditionReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/driver")
public class DriverController {
    
    @Autowired
    private TripLogService tripLogService;
    
    @Autowired
    private VehicleConditionReportService vehicleConditionReportService;
    
    @GetMapping("")
    public String driverDashboard(HttpSession session, Model model) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("DRIVER")) {
            return "redirect:/login";
        }
        
        model.addAttribute("userName", session.getAttribute("userName"));
        return "driver/driver-dashboard";
    }
    
    // Trip Log CRUD Operations
    @GetMapping("/triplogs")
    public String getAllTripLogs(HttpSession session, Model model) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("DRIVER")) {
            return "redirect:/login";
        }

        Long userId = (Long) session.getAttribute("userId");
        
        List<TripLog> tripLogs = tripLogService.getAllTripLogsByDriverId(userId);
        model.addAttribute("tripLogs", tripLogs);
        model.addAttribute("userName", session.getAttribute("userName"));
        return "driver/triplog-list";
    }
    
    @GetMapping("/triplogs/new")
    public String newTripLogForm(HttpSession session, Model model) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("DRIVER")) {
            return "redirect:/login";
        }

        
        model.addAttribute("tripLog", new TripLog());
        model.addAttribute("userName", session.getAttribute("userName"));
        return "driver/triplog-form";
    }
    
    @PostMapping("/triplogs")
    public String createTripLog(@ModelAttribute TripLog tripLog, HttpSession session) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("DRIVER")) {
            return "redirect:/login";
        }

        Long userId = (Long) session.getAttribute("userId");

        tripLog.setDriverId(userId);
        tripLogService.createTripLog(tripLog);
        return "redirect:/driver/triplogs";
    }
    
    @GetMapping("/triplogs/edit/{id}")
    public String editTripLogForm(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("DRIVER")) {
            return "redirect:/login";
        }
        
        Optional<TripLog> tripLog = tripLogService.getTripLogById(id);
        if (tripLog.isPresent()) {
            model.addAttribute("tripLog", tripLog.get());
            model.addAttribute("userName", session.getAttribute("userName"));
            return "driver/triplog-edit";
        }
        return "redirect:/driver/triplogs";
    }
    
    @PostMapping("/triplogs/edit/{id}")
    public String updateTripLog(@PathVariable Long id, @ModelAttribute TripLog tripLog, HttpSession session) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("DRIVER")) {
            return "redirect:/login";
        }

        Long userId = (Long) session.getAttribute("userId");
        
        tripLog.setDriverId(userId);
        tripLogService.updateTripLog(id, tripLog);
        return "redirect:/driver/triplogs";
    }
    
    @GetMapping("/triplogs/delete/{id}")
    public String deleteTripLog(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("DRIVER")) {
            return "redirect:/login";
        }
        
        tripLogService.deleteTripLog(id);
        return "redirect:/driver/triplogs";
    }
    
    // Vehicle Condition Report CRUD Operations
    @GetMapping("/reports")
    public String getAllReports(HttpSession session, Model model) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("DRIVER")) {
            return "redirect:/login";
        }

        Long userId = (Long) session.getAttribute("userId");
        
        List<VehicleConditionReport> reports = vehicleConditionReportService.getAllReportsByDriverId(userId);
        model.addAttribute("reports", reports);
        model.addAttribute("userName", session.getAttribute("userName"));
        return "driver/report-list";
    }
    
    @GetMapping("/reports/new")
    public String newReportForm(HttpSession session, Model model) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("DRIVER")) {
            return "redirect:/login";
        }
        
        model.addAttribute("report", new VehicleConditionReport());
        model.addAttribute("userName", session.getAttribute("userName"));
        return "driver/report-form";
    }
    
    @PostMapping("/reports")
    public String createReport(@ModelAttribute VehicleConditionReport report, HttpSession session) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("DRIVER")) {
            return "redirect:/login";
        }

        Long userId = (Long) session.getAttribute("userId");
        
        report.setDriverId(userId);
        vehicleConditionReportService.createReport(report);
        return "redirect:/driver/reports";
    }
    
    @GetMapping("/reports/edit/{id}")
    public String editReportForm(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("DRIVER")) {
            return "redirect:/login";
        }
        
        Optional<VehicleConditionReport> report = vehicleConditionReportService.getReportById(id);
        if (report.isPresent()) {
            model.addAttribute("report", report.get());
            model.addAttribute("userName", session.getAttribute("userName"));
            return "driver/report-edit";
        }
        return "redirect:/driver/reports";
    }
    
    @PostMapping("/reports/edit/{id}")
    public String updateReport(@PathVariable Long id, @ModelAttribute VehicleConditionReport report, HttpSession session) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("DRIVER")) {
            return "redirect:/login";
        }

        Long userId = (Long) session.getAttribute("userId");
        
        report.setDriverId(userId);
        vehicleConditionReportService.updateReport(id, report);
        return "redirect:/driver/reports";
    }
    
    @GetMapping("/reports/delete/{id}")
    public String deleteReport(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("DRIVER")) {
            return "redirect:/login";
        }
        
        vehicleConditionReportService.deleteReport(id);
        return "redirect:/driver/reports";
    }
}