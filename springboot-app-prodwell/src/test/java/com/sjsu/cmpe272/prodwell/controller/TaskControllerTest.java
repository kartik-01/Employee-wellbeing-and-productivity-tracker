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
import java.util.Optional;

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
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetTaskByTaskId() throws Exception {
        Task task = new Task();
        task.setTaskId("task-1");
        task.setTaskName("Test Task");

        when(taskService.getTaskByTaskId("task-1")).thenReturn(Optional.of(task));

        mockMvc.perform(get("/tasks/{taskId}", "task-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value("task-1"))
                .andExpect(jsonPath("$.taskName").value("Test Task"));
    }

    @Test
    void shouldReturn404WhenTaskNotFound() throws Exception {
        when(taskService.getTaskByTaskId("non-existent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/tasks/{taskId}", "non-existent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSaveTask() throws Exception {
        Task task = new Task();
        task.setTaskName("New Task");
        task.setUserId("user-1");

        when(taskService.saveTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/tasks/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateTask() throws Exception {
        Task task = new Task();
        task.setTaskId("task-1");
        task.setTaskName("Updated Task");

        when(taskService.updateTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(put("/tasks/{taskId}", "task-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value("task-1"))
                .andExpect(jsonPath("$.taskName").value("Updated Task"));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistentTask() throws Exception {
        Task task = new Task();
        task.setTaskId("non-existent");
        task.setTaskName("Updated Task");

        when(taskService.updateTask(any(Task.class))).thenReturn(null);

        mockMvc.perform(put("/tasks/{taskId}", "non-existent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask("task-1");

        mockMvc.perform(delete("/tasks/{taskId}", "task-1"))
                .andExpect(status().isNoContent());
    }
}