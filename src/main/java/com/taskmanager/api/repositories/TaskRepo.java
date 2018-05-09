package com.taskmanager.api.repositories;

import com.taskmanager.api.domains.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo extends JpaRepository<Task, Long> {
}
