package com.busSystem.BookingSchedule.passenger.service;

import com.busSystem.BookingSchedule.passenger.model.Feedback;
import com.busSystem.BookingSchedule.passenger.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {
    
    @Autowired
    private FeedbackRepository feedbackRepository;
    
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }
    
    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }
    
    public Feedback saveFeedback(Feedback feedback) {
        feedback.setUpdatedDate(LocalDateTime.now());
        if (feedback.getId() == null) {
            feedback.setCreatedDate(LocalDateTime.now());
        }
        return feedbackRepository.save(feedback);
    }
    
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }
    
    public List<Feedback> getFeedbacksByPassenger(Long passengerId) {
        return feedbackRepository.findByPassengerIdOrderByCreatedDateDesc(passengerId);
    }
    
    public List<Feedback> getFeedbacksByStatus(String status) {
        return feedbackRepository.findByStatus(status);
    }
    
    public List<Feedback> getFeedbacksByCategory(String category) {
        return feedbackRepository.findByCategory(category);
    }
    
    public boolean existsById(Long id) {
        return feedbackRepository.existsById(id);
    }
}