package com.busSystem.BookingSchedule.driver.service;

import com.busSystem.BookingSchedule.driver.model.TripLog;
import com.busSystem.BookingSchedule.driver.repository.TripLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TripLogService {
    
    @Autowired
    private TripLogRepository tripLogRepository;
    
    public List<TripLog> getAllTripLogsByDriverId(Long driverId) {
        return tripLogRepository.findActiveByDriverId(driverId);
    }
    
    public Optional<TripLog> getTripLogById(Long id) {
        return tripLogRepository.findById(id);
    }
    
    public TripLog createTripLog(TripLog tripLog) {
        tripLog.setCreatedDate(LocalDateTime.now());
        tripLog.setUpdatedDate(LocalDateTime.now());
        return tripLogRepository.save(tripLog);
    }
    
    public TripLog updateTripLog(Long id, TripLog updatedTripLog) {
        Optional<TripLog> existingTripLog = tripLogRepository.findById(id);
        if (existingTripLog.isPresent()) {
            TripLog tripLog = existingTripLog.get();
            tripLog.setRouteId(updatedTripLog.getRouteId());
            tripLog.setScheduleId(updatedTripLog.getScheduleId());
            tripLog.setStartTime(updatedTripLog.getStartTime());
            tripLog.setEndTime(updatedTripLog.getEndTime());
            tripLog.setPassengerCount(updatedTripLog.getPassengerCount());
            tripLog.setIssues(updatedTripLog.getIssues());
            tripLog.setUpdatedDate(LocalDateTime.now());
            return tripLogRepository.save(tripLog);
        }
        return null;
    }
    
    public boolean deleteTripLog(Long id) {
        Optional<TripLog> tripLog = tripLogRepository.findById(id);
        if (tripLog.isPresent()) {
            TripLog log = tripLog.get();
            log.setStatus("INACTIVE");
            log.setUpdatedDate(LocalDateTime.now());
            tripLogRepository.save(log);
            return true;
        }
        return false;
    }
}