package com.sjsu.cmpe272.prodwell.entity;

import org.junit.jupiter.api.Test;
import org.bson.types.ObjectId;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void shouldGenerateTaskIdWhenNull() {
        Task task = new Task();
        task.generateTaskId();
        assertNotNull(task.getTaskId());
    }

    @Test
    void shouldNotGenerateTaskIdWhenExists() {
        Task task = new Task();
        String existingId = "existing-id";
        task.setTaskId(existingId);
        task.generateTaskId();
        assertEquals(existingId, task.getTaskId());
    }

    @Test
    void shouldCreateTaskWithAllFields() {
        Task task = new Task();
        task.setId(new ObjectId());
        task.setTaskId("T1");
        task.setUserId("U1");
        task.setTaskName("Test Task");
        task.setAssignedDate(LocalDate.now());
        task.setDeadlineDate(LocalDate.now().plusDays(7));

        Map<String, Integer> hours = new HashMap<>();
        hours.put("2024-01-01", 8);
        task.setDailyHours(Arrays.asList(hours));

        assertNotNull(task.getId());
        assertEquals("T1", task.getTaskId());
        assertEquals("U1", task.getUserId());
        assertEquals(8, task.getDailyHours().get(0).get("2024-01-01"));
    }
}