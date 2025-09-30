package com.busSystem.BookingSchedule.passenger.service;

import com.busSystem.BookingSchedule.passenger.model.Booking;
import com.busSystem.BookingSchedule.passenger.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }
    
    public Booking saveBooking(Booking booking) {
        booking.setUpdatedDate(LocalDateTime.now());
        if (booking.getId() == null) {
            booking.setCreatedDate(LocalDateTime.now());
            booking.setBookingDate(LocalDateTime.now());
        }
        return bookingRepository.save(booking);
    }
    
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
    
    public List<Booking> getBookingsByPassenger(Long passengerId) {
        return bookingRepository.findByPassengerIdOrderByCreatedDateDesc(passengerId);
    }
    
    public List<Booking> getBookingsByPassengerAndStatus(Long passengerId, String status) {
        return bookingRepository.findByPassengerIdAndStatus(passengerId, status);
    }
    
    public List<Booking> getBookingsByDateRange(Long passengerId, LocalDateTime startDate, LocalDateTime endDate) {
        return bookingRepository.findByPassengerIdAndTravelDateBetween(passengerId, startDate, endDate);
    }
    
    public boolean existsById(Long id) {
        return bookingRepository.existsById(id);
    }
}