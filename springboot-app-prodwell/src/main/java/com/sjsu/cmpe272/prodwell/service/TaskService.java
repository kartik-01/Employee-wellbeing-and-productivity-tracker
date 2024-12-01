package com.sjsu.cmpe272.prodwell.service;

import com.sjsu.cmpe272.prodwell.entity.Task;
import com.sjsu.cmpe272.prodwell.repository.TaskRepository;
import com.sjsu.cmpe272.prodwell.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // Save a new task
    public Task saveTask(Task task) {
        if (!userRepository.existsByOid(task.getUserId())) {
            throw new IllegalArgumentException("Invalid userId: " + task.getUserId());
        }
        task.generateTaskId();
        return taskRepository.save(task);
    }

    // Bulk save tasks
    public List<Task> saveTasks(List<Task> tasks) {
        tasks.forEach(task -> {
            if (!userRepository.existsByOid(task.getUserId())) {
                throw new IllegalArgumentException("Invalid userOid: " + task.getUserId());
            }
            task.generateTaskId();
        });
        return taskRepository.saveAll(tasks);
    }

    // Get all tasks assigned to a user (by oid)
    public List<Task> getTasksByUserOid(String userOid) {
        return taskRepository.findByUserId(userOid);
    }

    // Get task by its ID
    public Task getTaskByTaskId(String taskId) {
        return taskRepository.findByTaskId(taskId);
    }

    // Update an existing task
    public Task updateTask(Task task) {
        if (!userRepository.existsByOid(task.getUserId())) {
            throw new IllegalArgumentException("Invalid userOid: " + task.getUserId());
        }
        if (task.getTaskId() == null) {
            task.generateTaskId();
        }
        return taskRepository.save(task);
    }

    // Delete task by its ID
    public void deleteTask(String taskId) {
        Task task = taskRepository.findByTaskId(taskId);
        if (task != null) {
            taskRepository.delete(task);
        }
    }
}
