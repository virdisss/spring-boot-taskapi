package com.taskmanager.api.dao;

import com.taskmanager.api.domains.Project;

import java.util.List;

public interface ProjectDAO {

    Project create(Project project);
    Project findOne(long id);
    Project update(long id, Project project);
    List<Project> findAll();
    void delete(long id);
}
