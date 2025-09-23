package com.busSystem.BookingSchedule.customerService.service;

import com.busSystem.BookingSchedule.customerService.model.ComplaintResponse;
import com.busSystem.BookingSchedule.customerService.repository.ComplaintResponseRepository;
import com.busSystem.BookingSchedule.passenger.model.Complaint;
import com.busSystem.BookingSchedule.passenger.service.ComplaintService;
import com.busSystem.BookingSchedule.passenger.service.BookingService;
import com.busSystem.BookingSchedule.passenger.model.Booking;
import com.busSystem.BookingSchedule.itSupport.service.UserService;
import com.busSystem.BookingSchedule.itSupport.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceComplaintService {
    
    @Autowired
    private ComplaintResponseRepository complaintResponseRepository;
    
    @Autowired
    private ComplaintService complaintService;
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private UserService userService;
    
    public List<Complaint> getAllComplaints() {
        return complaintService.getAllComplaints();
    }
    
    public Optional<Complaint> getComplaintById(Long id) {
        return complaintService.getComplaintById(id);
    }
    
    public List<Complaint> getComplaintsByStatus(String status) {
        return complaintService.getComplaintsByStatus(status);
    }
    
    public List<Complaint> getComplaintsByCategory(String category) {
        return complaintService.getComplaintsByCategory(category);
    }
    
    public Optional<Booking> getBookingByComplaint(Long complaintId) {
        Optional<Complaint> complaint = complaintService.getComplaintById(complaintId);
        if (complaint.isPresent() && complaint.get().getTicketId() != null) {
            return bookingService.getBookingById(complaint.get().getTicketId());
        }
        return Optional.empty();
    }
    
    public Optional<User> getPassengerByComplaint(Long complaintId) {
        Optional<Complaint> complaint = complaintService.getComplaintById(complaintId);
        if (complaint.isPresent()) {
            return userService.getUserById(complaint.get().getPassengerId());
        }
        return Optional.empty();
    }
    
    public Complaint updateComplaintStatus(Long complaintId, String status, String resolution) {
        Optional<Complaint> complaint = complaintService.getComplaintById(complaintId);
        if (complaint.isPresent()) {
            complaint.get().setStatus(status);
            complaint.get().setResolution(resolution);
            complaint.get().setUpdatedDate(LocalDateTime.now());
            return complaintService.saveComplaint(complaint.get());
        }
        return null;
    }
    
    public ComplaintResponse saveComplaintResponse(ComplaintResponse response) {
        response.setUpdatedDate(LocalDateTime.now());
        if (response.getId() == null) {
            response.setCreatedDate(LocalDateTime.now());
        }
        return complaintResponseRepository.save(response);
    }
    
    public List<ComplaintResponse> getResponsesByComplaint(Long complaintId) {
        return complaintResponseRepository.findByComplaintId(complaintId);
    }
}