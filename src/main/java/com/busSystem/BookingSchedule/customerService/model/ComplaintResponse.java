package com.busSystem.BookingSchedule.customerService.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "complaint_responses")
public class ComplaintResponse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "complaint_id")
    private Long complaintId;
    
    @Column(name = "customer_service_id")
    private Long customerServiceId;
    
    @Column(name = "response_message", columnDefinition = "TEXT")
    private String responseMessage;
    
    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    // Constructors
    public ComplaintResponse() {}
    
    public ComplaintResponse(Long complaintId, Long customerServiceId, String responseMessage, 
                           String internalNotes, String status) {
        this.complaintId = complaintId;
        this.customerServiceId = customerServiceId;
        this.responseMessage = responseMessage;
        this.internalNotes = internalNotes;
        this.status = status;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getComplaintId() { return complaintId; }
    public void setComplaintId(Long complaintId) { this.complaintId = complaintId; }
    
    public Long getCustomerServiceId() { return customerServiceId; }
    public void setCustomerServiceId(Long customerServiceId) { this.customerServiceId = customerServiceId; }
    
    public String getResponseMessage() { return responseMessage; }
    public void setResponseMessage(String responseMessage) { this.responseMessage = responseMessage; }
    
    public String getInternalNotes() { return internalNotes; }
    public void setInternalNotes(String internalNotes) { this.internalNotes = internalNotes; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
}