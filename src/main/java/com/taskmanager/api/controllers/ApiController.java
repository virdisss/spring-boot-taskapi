package com.taskmanager.api.controllers;

import com.taskmanager.api.dao.ProjectDAO;
import com.taskmanager.api.dao.TaskDAO;
import com.taskmanager.api.dao.UserDAO;
import com.taskmanager.api.domains.Project;
import com.taskmanager.api.domains.Task;
import com.taskmanager.api.domains.User;
import com.taskmanager.api.utils.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class ApiController {

    private UserDAO userDAO;
    private ProjectDAO projectDAO;
    private TaskDAO taskDAO;
    private PasswordEncoder passwordEncoder;

    public ApiController(UserDAO userDAO, ProjectDAO projectDAO, TaskDAO taskDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.projectDAO = projectDAO;
        this.taskDAO = taskDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("roles/{role_id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity register(@RequestBody User user, @PathVariable("role_id") long roleId) {

        if (this.findUserByUsername(user.getEmail()).toString().equalsIgnoreCase(ResponseEntity.ok(HttpStatus.NOT_FOUND).toString())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User res = userDAO.register(user);
            if (res == null) {
                return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
            }
            return ResponseEntity.ok(HttpStatus.CREATED);
        }
        return ResponseEntity.ok(HttpStatus.CONFLICT);
    }

    @GetMapping("/{email}")
    public ResponseEntity findUserByUsername(@PathVariable("email") String username) {
        User user = userDAO.findUserByUsername(username);

        if (user == null) {
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") long id, @RequestBody User user) {

        User res = userDAO.update(id, user);
        if (res == null) {
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{email}/logout")
    public ResponseEntity signout(@PathVariable("email") String username) {

        TokenManager.revokeToken(username);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /* -------------------- Projects --------------------- */
    @PostMapping("/{user_id}/projects")
    public ResponseEntity createProject(@PathVariable("user_id") long userId, @RequestBody Project project) {

        User user = userDAO.findOne(userId);
        project.getUsers().add(user);
        Project res = projectDAO.create(project);
        user.getProjects().add(res);
        userDAO.update(userId, user);
        if (res == null) {
            return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}/projects")
    public ResponseEntity loadProjects(@PathVariable("id") long id) {
        User user = userDAO.findOne(id);
        if (user == null) {
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user.getProjects());
    }

    /* -------------------- Tasks --------------------- */
    @PostMapping("/{user_id}/projects/{project_id}/tasks")
    public ResponseEntity createTask(@PathVariable("user_id") long userId, @PathVariable("project_id") long projectId, @RequestBody Task task) {

        User user = userDAO.findOne(userId);
        Project project = projectDAO.findOne(projectId);

        task.setProject(project);
        task.setUser(user);
        Task newTask = taskDAO.create(task);
        user.getTasks().add(newTask);
        project.getTasks().add(newTask);
        userDAO.update(userId, user);
        projectDAO.update(projectId, project);

        if (newTask == null) {
            return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("/projects/{project_id}/tasks")
    public ResponseEntity getTasksByProjectId(@PathVariable("project_id") long projectId) {

        Project project = projectDAO.findOne(projectId);
        if (project == null) {
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(project.getTasks());

    }

    @GetMapping("/tasks/{task_id}")
    public ResponseEntity getTaskById(@PathVariable("task_id") long taskId) {

        Task task = taskDAO.findOne(taskId);
        if (task == null) {
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(task);
    }

    @PostMapping("/tasks/{task_id}/edit")
    public ResponseEntity updateTaskById(@PathVariable("task_id") long taskId, @RequestBody Task task) {

        Task newTask = taskDAO.update(taskId, task);
        if (newTask == null) {
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(newTask);
    }

    @GetMapping("/tasks/{task_id}/delete")
    public void deleteTask(@PathVariable("task_id") long taskId) {
        taskDAO.delete(taskId);
    }
}
