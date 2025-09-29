package com.busSystem.BookingSchedule.ticketingOfficer.service;

import com.busSystem.BookingSchedule.ticketingOfficer.model.Ticket;
import com.busSystem.BookingSchedule.ticketingOfficer.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    
    @Autowired
    private TicketRepository ticketRepository;
    
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
    
    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }
    
    public Ticket saveTicket(Ticket ticket) {
        ticket.setUpdatedDate(LocalDateTime.now());
        if (ticket.getId() == null) {
            ticket.setCreatedDate(LocalDateTime.now());
            ticket.setBookingDate(LocalDateTime.now());
        }
        return ticketRepository.save(ticket);
    }
    
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
    
    public List<Ticket> getTicketsByPassenger(Long passengerId) {
        return ticketRepository.findByPassengerId(passengerId);
    }
    
    public List<Ticket> getTicketsByRoute(Long routeId) {
        return ticketRepository.findByRouteId(routeId);
    }
    
    public List<Ticket> getTicketsByType(String ticketType) {
        return ticketRepository.findByTicketType(ticketType);
    }
    
    public List<Ticket> getTicketsByStatus(String status) {
        return ticketRepository.findByStatus(status);
    }
    
    public List<Ticket> getTicketsByTravelDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return ticketRepository.findByTravelDateBetween(startDate, endDate);
    }
}