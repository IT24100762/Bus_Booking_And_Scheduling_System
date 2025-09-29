package com.busSystem.BookingSchedule.itSupport.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "backup_configs")
public class BackupConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String backupName;
    
    @Column(nullable = false)
    private String backupType;
    
    @Column(nullable = false)
    private String schedule;
    
    @Column(nullable = false)
    private String retentionPolicy;
    
    @Column(nullable = false)
    private String status;
    
    @Column(nullable = false)
    private LocalDateTime createdDate;
    
    @Column
    private LocalDateTime lastBackupDate;
    
    // Constructors
    public BackupConfig() {}
    
    public BackupConfig(String backupName, String backupType, String schedule, String retentionPolicy, String status) {
        this.backupName = backupName;
        this.backupType = backupType;
        this.schedule = schedule;
        this.retentionPolicy = retentionPolicy;
        this.status = status;
        this.createdDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getBackupName() { return backupName; }
    public void setBackupName(String backupName) { this.backupName = backupName; }
    
    public String getBackupType() { return backupType; }
    public void setBackupType(String backupType) { this.backupType = backupType; }
    
    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }
    
    public String getRetentionPolicy() { return retentionPolicy; }
    public void setRetentionPolicy(String retentionPolicy) { this.retentionPolicy = retentionPolicy; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public LocalDateTime getLastBackupDate() { return lastBackupDate; }
    public void setLastBackupDate(LocalDateTime lastBackupDate) { this.lastBackupDate = lastBackupDate; }
}