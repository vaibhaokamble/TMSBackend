package com.organization.taskManagement.Repository;

import com.organization.taskManagement.Model.ProjectModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectModel, Long> {
}
