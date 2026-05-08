package com.organization.taskManagement.Repository;

import com.organization.taskManagement.Enums.TaskStatus;
import com.organization.taskManagement.Model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskModel, Long> {
    
    @Query("SELECT COUNT(t) FROM TaskModel t WHERE t.status = ?1")
    Long countByStatus(TaskStatus status);
    
    @Query("SELECT t FROM TaskModel t WHERE t.assignedTo IS NOT NULL")
    List<TaskModel> findAllAssignedTasks();
}
