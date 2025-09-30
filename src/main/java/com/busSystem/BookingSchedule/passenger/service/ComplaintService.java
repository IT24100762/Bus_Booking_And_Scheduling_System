package com.busSystem.BookingSchedule.passenger.service;

import com.busSystem.BookingSchedule.passenger.model.Complaint;
import com.busSystem.BookingSchedule.passenger.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ComplaintService {
    
    @Autowired
    private ComplaintRepository complaintRepository;
    
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }
    
    public Optional<Complaint> getComplaintById(Long id) {
        return complaintRepository.findById(id);
    }
    
    public Complaint saveComplaint(Complaint complaint) {
        complaint.setUpdatedDate(LocalDateTime.now());
        if (complaint.getId() == null) {
            complaint.setCreatedDate(LocalDateTime.now());
        }
        return complaintRepository.save(complaint);
    }
    
    public void deleteComplaint(Long id) {
        complaintRepository.deleteById(id);
    }
    
    public List<Complaint> getComplaintsByPassenger(Long passengerId) {
        return complaintRepository.findByPassengerIdOrderByCreatedDateDesc(passengerId);
    }
    
    public List<Complaint> getComplaintsByStatus(String status) {
        return complaintRepository.findByStatus(status);
    }
    
    public List<Complaint> getComplaintsByCategory(String category) {
        return complaintRepository.findByCategory(category);
    }
    
    public boolean existsById(Long id) {
        return complaintRepository.existsById(id);
    }
}