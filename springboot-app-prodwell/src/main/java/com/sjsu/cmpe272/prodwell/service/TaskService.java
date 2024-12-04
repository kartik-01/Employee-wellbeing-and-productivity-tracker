package com.sjsu.cmpe272.prodwell.service;

import com.sjsu.cmpe272.prodwell.entity.Task;
import com.sjsu.cmpe272.prodwell.repository.TaskRepository;
import com.sjsu.cmpe272.prodwell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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
    public Optional<Task> getTaskByTaskId(String taskId) {
        return taskRepository.findByTaskId(taskId);
    }

     // Update an existing task
     public Task updateTask(Task task) {
        Optional<Task> existingTaskOptional = taskRepository.findByTaskId(task.getTaskId());
        if (existingTaskOptional.isEmpty()) {
            return null; // Task not found, return null
        }

        Task existingTask = existingTaskOptional.get();
        // Update fields from the input task object
        existingTask.setTaskName(task.getTaskName());
        existingTask.setAssignedDate(task.getAssignedDate());
        existingTask.setDeadlineDate(task.getDeadlineDate());
        existingTask.setTaskStartDate(task.getTaskStartDate());
        existingTask.setTaskEndDate(task.getTaskEndDate());
        existingTask.setDailyHours(task.getDailyHours());

        return taskRepository.save(existingTask); // Save updated task to the repository
    }

     // Delete task by its ID
     public void deleteTask(String taskId) {
        Optional<Task> taskOptional = taskRepository.findByTaskId(taskId);
        if (taskOptional.isPresent()) {
            taskRepository.delete(taskOptional.get());
        }
    }
}
