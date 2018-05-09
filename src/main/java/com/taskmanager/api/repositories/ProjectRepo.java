package com.taskmanager.api.repositories;

import com.taskmanager.api.domains.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepo extends JpaRepository<Project, Long> {
}
