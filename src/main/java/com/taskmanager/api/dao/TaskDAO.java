package com.taskmanager.api.dao;

import com.taskmanager.api.domains.Task;

import java.util.List;

public interface TaskDAO {

    Task create(Task task);
    Task findOne(long id);
    Task update(long id, Task task);
    List<Task> findAll();
    void delete(long id);
}
