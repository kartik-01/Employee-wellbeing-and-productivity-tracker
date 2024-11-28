package com.sjsu.cmpe272.prodwell.controller;

import com.sjsu.cmpe272.prodwell.entity.Task;
import com.sjsu.cmpe272.prodwell.service.TaskService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Endpoint to create a new task
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody Task task) {
        return taskService.saveTask(task);
    }

    // Endpoint to bulk upload tasks
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Task> createTasks(@RequestBody List<Task> tasks) {
        return taskService.saveTasks(tasks);
    }

    // Endpoint to get all tasks assigned to a specific user
    @GetMapping("/user/{userId}")
    public List<Task> getTasksByUserId(@PathVariable ObjectId userId) {
        return taskService.getTasksByUserId(userId);
    }

    // Endpoint to get a task by its ID
    @GetMapping("/{taskId}")
    public Task getTaskById(@PathVariable ObjectId taskId) {
        return taskService.getTaskById(taskId);
    }

    // Endpoint to update an existing task
    @PutMapping("/{taskId}")
    public Task updateTask(@PathVariable ObjectId taskId, @RequestBody Task task) {
        task.setId(taskId);
        return taskService.updateTask(task);
    }

    // Endpoint to delete a task by its ID
    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable ObjectId taskId) {
        taskService.deleteTask(taskId);
    }
}
