package com.organization.taskManagement.Services;

import com.organization.taskManagement.Model.ActivityLogModel;
import com.organization.taskManagement.Model.EmployeeRegisterModel;
import com.organization.taskManagement.Model.TaskModel;
import com.organization.taskManagement.Repository.ActivityLogRepository;
import com.organization.taskManagement.Repository.EmployeeRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityLogService {

    @Autowired
    private ActivityLogRepository activityLogRepository;
    
    @Autowired
    private EmployeeRegisterRepository employeeRegRepo;

    public void logActivity(String action, String message, TaskModel task) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        EmployeeRegisterModel employee = null;
        if (auth != null && auth.getName() != null && !auth.getName().equals("anonymousUser")) {
            employee = employeeRegRepo.findByEmployeeId(auth.getName()).orElse(null);
        }
        logActivity(employee, action, message, task);
    }
    
    public void logActivity(EmployeeRegisterModel employee, String action, String message, TaskModel task) {
        ActivityLogModel log = new ActivityLogModel();
        log.setEmployeeId(employee);
        log.setAction(action);
        log.setMessage(message);
        log.setTimestamp(LocalDateTime.now());
        log.setTask(task);
        activityLogRepository.save(log);
    }
    
    public void logActivity(String action, String message) {
        logActivity(action, message, null);
    }
    
    public void logActivity(EmployeeRegisterModel employee, String action, String message) {
        logActivity(employee, action, message, null);
    }

    public List<ActivityLogModel> getAllLogs() {
        return activityLogRepository.findAllByOrderByTimestampDesc();
    }
}
