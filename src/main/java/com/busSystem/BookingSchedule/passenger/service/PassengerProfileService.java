package com.busSystem.BookingSchedule.passenger.service;

import com.busSystem.BookingSchedule.passenger.model.PassengerProfile;
import com.busSystem.BookingSchedule.passenger.repository.PassengerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PassengerProfileService {
    
    @Autowired
    private PassengerProfileRepository passengerProfileRepository;
    
    public List<PassengerProfile> getAllProfiles() {
        return passengerProfileRepository.findAll();
    }
    
    public Optional<PassengerProfile> getProfileById(Long id) {
        return passengerProfileRepository.findById(id);
    }
    
    public Optional<PassengerProfile> getProfileByUserId(Long userId) {
        return passengerProfileRepository.findByUserId(userId);
    }
    
    public PassengerProfile saveProfile(PassengerProfile profile) {
        profile.setUpdatedDate(LocalDateTime.now());
        if (profile.getId() == null) {
            profile.setCreatedDate(LocalDateTime.now());
        }
        return passengerProfileRepository.save(profile);
    }
    
    public void deleteProfile(Long id) {
        passengerProfileRepository.deleteById(id);
    }
    
    public boolean existsByUserId(Long userId) {
        return passengerProfileRepository.existsByUserId(userId);
    }
}