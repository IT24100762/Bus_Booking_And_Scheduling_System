package com.busSystem.BookingSchedule.itSupport.controller;

import com.busSystem.BookingSchedule.itSupport.model.BackupConfig;
import com.busSystem.BookingSchedule.itSupport.service.BackupConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/itsupport/backups")
public class BackupManagementController {
    
    @Autowired
    private BackupConfigService backupConfigService;
    
    @GetMapping
    public String listBackups(HttpSession session, Model model,
                             @RequestParam(required = false) String status,
                             @RequestParam(required = false) String type) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("IT_SUPPORT")) {
            return "redirect:/login";
        }
        
        if (status != null && !status.isEmpty()) {
            model.addAttribute("backups", backupConfigService.getBackupConfigsByStatus(status));
        } else if (type != null && !type.isEmpty()) {
            model.addAttribute("backups", backupConfigService.getBackupConfigsByType(type));
        } else {
            model.addAttribute("backups", backupConfigService.getAllBackupConfigs());
        }
        
        return "itSupport/backups/list";
    }
    
    @GetMapping("/create")
    public String createBackupForm(HttpSession session, Model model) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("IT_SUPPORT")) {
            return "redirect:/login";
        }
        model.addAttribute("backup", new BackupConfig());
        return "itSupport/backups/create";
    }
    
    @PostMapping("/create")
    public String createBackup(HttpSession session, @ModelAttribute BackupConfig backup) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("IT_SUPPORT")) {
            return "redirect:/login";
        }
        backupConfigService.saveBackupConfig(backup);
        return "redirect:/itsupport/backups";
    }
    
    @GetMapping("/edit/{id}")
    public String editBackupForm(HttpSession session, @PathVariable Long id, Model model) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("IT_SUPPORT")) {
            return "redirect:/login";
        }
        Optional<BackupConfig> backup = backupConfigService.getBackupConfigById(id);
        if (backup.isPresent()) {
            model.addAttribute("backup", backup.get());
            return "itSupport/backups/edit";
        }
        return "redirect:/itsupport/backups";
    }
    
    @PostMapping("/edit/{id}")
    public String editBackup(HttpSession session, @PathVariable Long id, @ModelAttribute BackupConfig backup) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("IT_SUPPORT")) {
            return "redirect:/login";
        }
        backup.setId(id);
        backupConfigService.saveBackupConfig(backup);
        return "redirect:/itsupport/backups";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteBackup(HttpSession session, @PathVariable Long id) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("IT_SUPPORT")) {
            return "redirect:/login";
        }
        backupConfigService.deleteBackupConfig(id);
        return "redirect:/itsupport/backups";
    }
    
    @GetMapping("/view/{id}")
    public String viewBackup(HttpSession session, @PathVariable Long id, Model model) {
        if (session.getAttribute("userRole") == null || !session.getAttribute("userRole").equals("IT_SUPPORT")) {
            return "redirect:/login";
        }
        Optional<BackupConfig> backup = backupConfigService.getBackupConfigById(id);
        if (backup.isPresent()) {
            model.addAttribute("backup", backup.get());
            return "itSupport/backups/view";
        }
        return "redirect:/itsupport/backups";
    }
}