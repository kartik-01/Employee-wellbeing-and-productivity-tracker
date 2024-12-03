package com.sjsu.cmpe272.prodwell.service;

import com.sjsu.cmpe272.prodwell.entity.Task;
import com.sjsu.cmpe272.prodwell.repository.TaskRepository;
import com.sjsu.cmpe272.prodwell.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService service;

    @Test
    void shouldSaveTask() {
        Task task = new Task();
        task.setUserId("user-1");
        task.setTaskName("Test Task");

        when(userRepository.existsByOid("user-1")).thenReturn(true);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = service.saveTask(task);
        assertNotNull(result);
        assertNotNull(result.getTaskId());
    }

    @Test
    void shouldSaveMultipleTasks() {
        List<Task> tasks = Arrays.asList(
                createTask("user-1", "Task 1"),
                createTask("user-1", "Task 2")
        );

        when(userRepository.existsByOid("user-1")).thenReturn(true);
        when(taskRepository.saveAll(anyList())).thenReturn(tasks);

        List<Task> results = service.saveTasks(tasks);
        assertEquals(2, results.size());
    }

    private Task createTask(String userId, String taskName) {
        Task task = new Task();
        task.setUserId(userId);
        task.setTaskName(taskName);
        return task;
    }

    @Test
    void shouldGetTaskByTaskId() {
        Task task = new Task();
        task.setTaskId("task-1");
        when(taskRepository.findByTaskId("task-1")).thenReturn(task);

        Task result = service.getTaskByTaskId("task-1");
        assertNotNull(result);
    }

    @Test
    void shouldUpdateExistingTask() {
        Task task = new Task();
        task.setTaskId("task-1");
        task.setUserId("user-1");

        when(userRepository.existsByOid("user-1")).thenReturn(true);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = service.updateTask(task);
        assertNotNull(result);
    }

    @Test
    void shouldDeleteTask() {
        Task task = new Task();
        when(taskRepository.findByTaskId("task-1")).thenReturn(task);

        service.deleteTask("task-1");
        verify(taskRepository).delete(task);
    }

}