package com.busSystem.BookingSchedule.ticketingOfficer.service;

import com.busSystem.BookingSchedule.ticketingOfficer.model.Fare;
import com.busSystem.BookingSchedule.ticketingOfficer.repository.FareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FareService {
    
    @Autowired
    private FareRepository fareRepository;
    
    public List<Fare> getAllFares() {
        return fareRepository.findAll();
    }
    
    public Optional<Fare> getFareById(Long id) {
        return fareRepository.findById(id);
    }
    
    public Fare saveFare(Fare fare) {
        fare.setUpdatedDate(LocalDateTime.now());
        if (fare.getId() == null) {
            fare.setCreatedDate(LocalDateTime.now());
        }
        return fareRepository.save(fare);
    }
    
    public void deleteFare(Long id) {
        fareRepository.deleteById(id);
    }
    
    public List<Fare> getFaresByRoute(Long routeId) {
        return fareRepository.findByRouteId(routeId);
    }
    
    public List<Fare> getFaresByType(String ticketType) {
        return fareRepository.findByTicketType(ticketType);
    }
    
    public List<Fare> getFaresByStatus(String status) {
        return fareRepository.findByStatus(status);
    }
    
    public List<Fare> getActiveFaresByRouteAndType(Long routeId, String ticketType) {
        return fareRepository.findActiveByRouteIdAndTicketType(routeId, ticketType);
    }
    
    public List<Fare> getValidFares() {
        return fareRepository.findValidFares(LocalDateTime.now());
    }
}