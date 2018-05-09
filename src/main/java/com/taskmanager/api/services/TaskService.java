package com.taskmanager.api.services;

import com.taskmanager.api.dao.TaskDAO;
import com.taskmanager.api.domains.Task;
import com.taskmanager.api.repositories.TaskRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService implements TaskDAO {

    private TaskRepo taskRepo;

    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    @Override
    public Task create(Task task) {
        return taskRepo.save(task);
    }

    @Override
    public Task findOne(long id) {
        return taskRepo.getOne(id);
    }

    @Override
    public List<Task> findAll() {
        return taskRepo.findAll();
    }

    @Override
    public void delete(long id) {
        taskRepo.deleteById(id);
    }

    @Override
    public Task update(long id, Task task) {
        Task newTask = taskRepo.getOne(id);
        newTask.setDescription(task.getDescription());
        newTask.setName(task.getName());
        return taskRepo.save(newTask);
    }
}
