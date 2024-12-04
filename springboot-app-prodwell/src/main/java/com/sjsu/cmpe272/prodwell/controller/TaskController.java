package com.sjsu.cmpe272.prodwell.controller;

import com.sjsu.cmpe272.prodwell.entity.Task;
import com.sjsu.cmpe272.prodwell.service.TaskService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "${cors.allowed-origins}")
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Endpoint to create a new task
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Task saveTask(@RequestBody Task task) {
        return taskService.saveTask(task);
    }

    // Endpoint to bulk upload tasks
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Task> createTasks(@RequestBody List<Task> tasks) {
        return taskService.saveTasks(tasks);
    }

    // Endpoint to get all tasks assigned to a specific user
    @GetMapping("/user/{userOid}")
    public List<Task> getTasksByUserOid(@PathVariable String userOid) {
        return taskService.getTasksByUserOid(userOid);
    }

    // Endpoint to get a task by its ID
    @GetMapping("/{taskId}")
    public Task getTaskByTaskId(@PathVariable String taskId) {
        return taskService.getTaskByTaskId(taskId);
    }

    // Endpoint to update an existing task
    @PutMapping("/{taskId}")
    public Task updateTask(@PathVariable String taskId, @RequestBody Task task) {
        task.setTaskId(taskId);
        return taskService.updateTask(task);
    }

    // Endpoint to delete a task by its ID
    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable String taskId) {
        taskService.deleteTask(taskId);
    }
}
