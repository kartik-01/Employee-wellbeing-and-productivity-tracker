package com.sjsu.cmpe272.prodwell.service;

import com.sjsu.cmpe272.prodwell.entity.Task;
import com.sjsu.cmpe272.prodwell.repository.TaskRepository;
import com.sjsu.cmpe272.prodwell.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.never;

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
    void shouldThrowExceptionForInvalidUserId() {
        Task task = new Task();
        task.setUserId("invalid-user");

        when(userRepository.existsByOid("invalid-user")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> service.saveTask(task));
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

    @Test
    void shouldGetTasksByUserOid() {
        List<Task> tasks = Arrays.asList(
                createTask("user-1", "Task 1"),
                createTask("user-1", "Task 2")
        );

        when(taskRepository.findByUserId("user-1")).thenReturn(tasks);

        List<Task> results = service.getTasksByUserOid("user-1");
        assertEquals(2, results.size());
    }

    @Test
    void shouldGetTaskByTaskId() {
        Task task = new Task();
        task.setTaskId("task-1");

        when(taskRepository.findByTaskId("task-1")).thenReturn(Optional.of(task));

        Optional<Task> result = service.getTaskByTaskId("task-1");
        assertTrue(result.isPresent());
        assertEquals("task-1", result.get().getTaskId());
    }

    @Test
    void shouldReturnEmptyOptionalWhenTaskNotFound() {
        when(taskRepository.findByTaskId("non-existent")).thenReturn(Optional.empty());
        Optional<Task> result = service.getTaskByTaskId("non-existent");
        assertFalse(result.isPresent());
    }

    @Test
    void shouldUpdateExistingTask() {
        Task existingTask = new Task();
        existingTask.setTaskId("task-1");

        Task updatedTask = new Task();
        updatedTask.setTaskId("task-1");
        updatedTask.setTaskName("Updated Task");
        updatedTask.setAssignedDate(LocalDate.now());
        updatedTask.setDeadlineDate(LocalDate.now().plusDays(1));

        when(taskRepository.findByTaskId("task-1")).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task result = service.updateTask(updatedTask);
        assertNotNull(result);
        assertEquals("Updated Task", result.getTaskName());
    }

    @Test
    void shouldReturnNullWhenUpdatingNonExistentTask() {
        Task task = new Task();
        task.setTaskId("non-existent");

        when(taskRepository.findByTaskId("non-existent")).thenReturn(Optional.empty());

        Task result = service.updateTask(task);
        assertNull(result);
    }

    @Test
    void shouldDeleteTask() {
        Task task = new Task();
        task.setTaskId("task-1");
        when(taskRepository.findByTaskId("task-1")).thenReturn(Optional.of(task));

        service.deleteTask("task-1");
        verify(taskRepository).delete(task);
    }

    @Test
    void shouldNotDeleteWhenTaskNotFound() {
        when(taskRepository.findByTaskId("non-existent")).thenReturn(Optional.empty());

        service.deleteTask("non-existent");
        verify(taskRepository, never()).delete(any());
    }

    private Task createTask(String userId, String taskName) {
        Task task = new Task();
        task.setUserId(userId);
        task.setTaskName(taskName);
        return task;
    }
}