package com.busSystem.BookingSchedule.driver.service;

import com.busSystem.BookingSchedule.driver.model.VehicleConditionReport;
import com.busSystem.BookingSchedule.driver.repository.VehicleConditionReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleConditionReportService {
    
    @Autowired
    private VehicleConditionReportRepository vehicleConditionReportRepository;
    
    public List<VehicleConditionReport> getAllReportsByDriverId(Long driverId) {
        return vehicleConditionReportRepository.findActiveByDriverId(driverId);
    }
    
    public Optional<VehicleConditionReport> getReportById(Long id) {
        return vehicleConditionReportRepository.findById(id);
    }
    
    public VehicleConditionReport createReport(VehicleConditionReport report) {
        report.setCreatedDate(LocalDateTime.now());
        report.setUpdatedDate(LocalDateTime.now());
        return vehicleConditionReportRepository.save(report);
    }
    
    public VehicleConditionReport updateReport(Long id, VehicleConditionReport updatedReport) {
        Optional<VehicleConditionReport> existingReport = vehicleConditionReportRepository.findById(id);
        if (existingReport.isPresent()) {
            VehicleConditionReport report = existingReport.get();
            report.setVehicleNumber(updatedReport.getVehicleNumber());
            report.setBrakesCondition(updatedReport.getBrakesCondition());
            report.setLightsCondition(updatedReport.getLightsCondition());
            report.setTiresCondition(updatedReport.getTiresCondition());
            report.setEngineCondition(updatedReport.getEngineCondition());
            report.setOtherIssues(updatedReport.getOtherIssues());
            report.setOverallStatus(updatedReport.getOverallStatus());
            report.setFollowUpNotes(updatedReport.getFollowUpNotes());
            report.setUpdatedDate(LocalDateTime.now());
            return vehicleConditionReportRepository.save(report);
        }
        return null;
    }
    
    public boolean deleteReport(Long id) {
        Optional<VehicleConditionReport> report = vehicleConditionReportRepository.findById(id);
        if (report.isPresent()) {
            VehicleConditionReport r = report.get();
            r.setStatus("INACTIVE");
            r.setUpdatedDate(LocalDateTime.now());
            vehicleConditionReportRepository.save(r);
            return true;
        }
        return false;
    }
}