package com.taskmanager.api.repositories;

import com.taskmanager.api.domains.Task;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author vantenor
 */
public interface RoleRepo extends JpaRepository<Task, Long> {

}
