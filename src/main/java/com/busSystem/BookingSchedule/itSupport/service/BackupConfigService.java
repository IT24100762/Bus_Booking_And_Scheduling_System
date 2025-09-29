package com.busSystem.BookingSchedule.itSupport.service;

import com.busSystem.BookingSchedule.itSupport.model.BackupConfig;
import com.busSystem.BookingSchedule.itSupport.repository.BackupConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BackupConfigService {
    
    @Autowired
    private BackupConfigRepository backupConfigRepository;
    
    public List<BackupConfig> getAllBackupConfigs() {
        return backupConfigRepository.findAll();
    }
    
    public Optional<BackupConfig> getBackupConfigById(Long id) {
        return backupConfigRepository.findById(id);
    }
    
    public BackupConfig saveBackupConfig(BackupConfig backupConfig) {
        if (backupConfig.getId() == null) {
            backupConfig.setCreatedDate(LocalDateTime.now());
        }
        return backupConfigRepository.save(backupConfig);
    }
    
    public void deleteBackupConfig(Long id) {
        backupConfigRepository.deleteById(id);
    }
    
    public List<BackupConfig> getBackupConfigsByStatus(String status) {
        return backupConfigRepository.findByStatus(status);
    }
    
    public List<BackupConfig> getBackupConfigsByType(String backupType) {
        return backupConfigRepository.findByBackupType(backupType);
    }
}