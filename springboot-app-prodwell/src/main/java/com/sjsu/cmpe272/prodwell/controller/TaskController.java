package com.sjsu.cmpe272.prodwell.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sjsu.cmpe272.prodwell.entity.Task;
import com.sjsu.cmpe272.prodwell.service.TaskService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/tasks")
public class TaskController {

	@Autowired
	private TaskService service;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Task createTask(@RequestBody Task task) {
		return service.addTask(task);
	}
	
	@GetMapping
	public List<Task> getTasks(){
		return service.findAllTasks();
	}
	
	@GetMapping("/{taskId}")
	public Task getTask(String taskId) {
		return service.getTasksByTaskId(taskId);
	}
	
	@GetMapping("/severity/{severity}")
    public List<Task> getTasksBySeverity(@PathVariable int severity) {
        return service.getTaskBySeverity(severity);
    }
    
    @GetMapping("/assignee/{assignee}")
    public List<Task> getTasksByAssignee(@PathVariable String assignee) {
        return service.getTaskByAssignee(assignee);
    }
    
    @PutMapping
    public Task updateTask(@RequestBody Task task) {
        return service.updateTask(task);
    }
    
    @DeleteMapping("/{taskId}")
    public String deleteTask(@PathVariable String taskId) {
        return service.deleteTask(taskId);
    }
	
}
