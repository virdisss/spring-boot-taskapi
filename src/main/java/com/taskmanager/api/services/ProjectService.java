package com.taskmanager.api.services;

import com.taskmanager.api.dao.ProjectDAO;
import com.taskmanager.api.domains.Project;
import com.taskmanager.api.domains.Task;
import com.taskmanager.api.repositories.ProjectRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService implements ProjectDAO {

    private ProjectRepo projectRepo;

    public ProjectService(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
    }

    @Override
    public Project create(Project project) {
        return projectRepo.save(project);
    }

    @Override
    public Project findOne(long id) {
        return projectRepo.getOne(id);
    }

    @Override
    public List<Project> findAll() {
        return projectRepo.findAll();
    }

    @Override
    public void delete(long id) {
        projectRepo.deleteById(id);
    }

    @Override
    public Project update(long id, Project project) {
        Project newProject = projectRepo.getOne(id);
        newProject.setDescription(project.getDescription());
        newProject.setName(project.getName());
        return projectRepo.save(newProject);
    }
}
