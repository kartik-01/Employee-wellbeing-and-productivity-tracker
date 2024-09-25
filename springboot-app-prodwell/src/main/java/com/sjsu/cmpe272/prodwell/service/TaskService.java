package com.sjsu.cmpe272.prodwell.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sjsu.cmpe272.prodwell.entity.Task;
import com.sjsu.cmpe272.prodwell.repository.TaskRepository;

@Service
public class TaskService {
	@Autowired
	private TaskRepository repository;
	// CRUD
	
	public Task addTask(Task task) {
		task.setTaskId(UUID.randomUUID().toString().split("-")[0]);
		return repository.save(task);
	}
	
	public List<Task> findAllTasks(){
		return repository.findAll();
	}
	
	public Task getTasksByTaskId(String taskId) {
		return repository.findById(taskId).get();
	}
	
	public List<Task> getTaskBySeverity(int severity) {
		return repository.findBySeverity(severity);
	}
	
	public List<Task> getTaskByAssignee(String assignee) {
		return repository.getTasksByAssignee(assignee);
	}
	
	public Task updateTask(Task taskRequest) {
		//  get the existing document from mongodb
		//populate the new value to existing entity
		Task existingTask = repository.findById(taskRequest.getTaskId()).get();
		existingTask.setDescription(taskRequest.getDescription());
		existingTask.setSeverity(taskRequest.getSeverity());
		existingTask.setAssignee(taskRequest.getAssignee());
		existingTask.setStoryPoint(taskRequest.getStoryPoint());
		return repository.save(existingTask);
	}
	
	public String deleteTask(String taskId) {
		repository.deleteById(taskId);
		return taskId+" task deleted from dashboard ";
	}
}
