package com.busSystem.BookingSchedule.customerService.service;

import com.busSystem.BookingSchedule.customerService.model.FeedbackResponse;
import com.busSystem.BookingSchedule.customerService.repository.FeedbackResponseRepository;
import com.busSystem.BookingSchedule.passenger.model.Feedback;
import com.busSystem.BookingSchedule.passenger.service.FeedbackService;
import com.busSystem.BookingSchedule.itSupport.service.UserService;
import com.busSystem.BookingSchedule.itSupport.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceFeedbackService {
    
    @Autowired
    private FeedbackResponseRepository feedbackResponseRepository;
    
    @Autowired
    private FeedbackService feedbackService;
    
    @Autowired
    private UserService userService;
    
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }
    
    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackService.getFeedbackById(id);
    }
    
    public List<Feedback> getFeedbacksByStatus(String status) {
        return feedbackService.getFeedbacksByStatus(status);
    }
    
    public List<Feedback> getFeedbacksByCategory(String category) {
        return feedbackService.getFeedbacksByCategory(category);
    }
    
    public Optional<User> getPassengerByFeedback(Long feedbackId) {
        Optional<Feedback> feedback = feedbackService.getFeedbackById(feedbackId);
        if (feedback.isPresent()) {
            return userService.getUserById(feedback.get().getPassengerId());
        }
        return Optional.empty();
    }
    
    public Feedback updateFeedbackStatus(Long feedbackId, String status) {
        Optional<Feedback> feedback = feedbackService.getFeedbackById(feedbackId);
        if (feedback.isPresent()) {
            feedback.get().setStatus(status);
            feedback.get().setUpdatedDate(LocalDateTime.now());
            return feedbackService.saveFeedback(feedback.get());
        }
        return null;
    }
    
    public FeedbackResponse saveFeedbackResponse(FeedbackResponse response) {
        response.setUpdatedDate(LocalDateTime.now());
        if (response.getId() == null) {
            response.setCreatedDate(LocalDateTime.now());
        }
        return feedbackResponseRepository.save(response);
    }
    
    public List<FeedbackResponse> getResponsesByFeedback(Long feedbackId) {
        return feedbackResponseRepository.findByFeedbackId(feedbackId);
    }
}