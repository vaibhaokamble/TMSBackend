package com.organization.taskManagement.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;



@Entity
@Table(name = "activity_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityLogModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeRegisterModel employeeId;
    private String action;
    private String message;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private TaskModel task;
}
