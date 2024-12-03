package com.sjsu.cmpe272.prodwell.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sjsu.cmpe272.prodwell.entity.Task;
import com.sjsu.cmpe272.prodwell.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService service;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCreateTask() throws Exception {
        Task task = new Task();
        task.setTaskId("task-1");
        task.setUserId("user-1");
        task.setTaskName("Test Task");
        task.setAssignedDate(LocalDate.now());
        task.setDeadlineDate(LocalDate.now().plusDays(7));

        when(service.saveTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/tasks/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.taskId").value("task-1"))
                .andExpect(jsonPath("$.taskName").value("Test Task"));
    }

    @Test
    void shouldCreateBulkTasks() throws Exception {
        List<Task> tasks = Arrays.asList(
                createTask("task-1", "Test Task 1"),
                createTask("task-2", "Test Task 2")
        );

        when(service.saveTasks(any())).thenReturn(tasks);

        mockMvc.perform(post("/tasks/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tasks)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].taskId").value("task-1"))
                .andExpect(jsonPath("$[1].taskId").value("task-2"));
    }

    @Test
    void shouldGetUserTasks() throws Exception {
        Task task = createTask("task-1", "Test Task");
        when(service.getTasksByUserOid("user-1")).thenReturn(Arrays.asList(task));

        mockMvc.perform(get("/tasks/user/user-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].taskId").value("task-1"))
                .andExpect(jsonPath("$[0].taskName").value("Test Task"));
    }

    @Test
    void shouldGetTaskById() throws Exception {
        Task task = createTask("task-1", "Test Task");
        when(service.getTaskByTaskId("task-1")).thenReturn(task);

        mockMvc.perform(get("/tasks/task-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value("task-1"))
                .andExpect(jsonPath("$.taskName").value("Test Task"));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        Task task = createTask("task-1", "Updated Task");
        when(service.updateTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(put("/tasks/task-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskName").value("Updated Task"));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        doNothing().when(service).deleteTask("task-1");

        mockMvc.perform(delete("/tasks/task-1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteTask("task-1");
    }

    private Task createTask(String taskId, String taskName) {
        Task task = new Task();
        task.setTaskId(taskId);
        task.setUserId("user-1");
        task.setTaskName(taskName);
        task.setAssignedDate(LocalDate.now());
        task.setDeadlineDate(LocalDate.now().plusDays(7));
        return task;
    }
}