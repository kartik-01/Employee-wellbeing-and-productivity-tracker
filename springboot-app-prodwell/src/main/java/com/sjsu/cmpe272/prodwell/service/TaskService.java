package com.sjsu.cmpe272.prodwell.service;

import com.sjsu.cmpe272.prodwell.entity.Task;
import com.sjsu.cmpe272.prodwell.repository.TaskRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    // Save a new task
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    // Get all tasks assigned to a user
    public List<Task> getTasksByUserId(ObjectId userId) {
        return taskRepository.findByUserId(userId);
    }

    // Get task by its ID
    public Task getTaskById(ObjectId id) {
        return taskRepository.findById(id).orElse(null);
    }

    // Update an existing task
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    // Delete task by its ID
    public void deleteTask(ObjectId id) {
        taskRepository.deleteById(id);
    }
}
