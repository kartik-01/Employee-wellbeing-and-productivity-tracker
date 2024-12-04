package com.sjsu.cmpe272.prodwell.service;
import com.sjsu.cmpe272.prodwell.dto.ManagerAnalyticsDTO;
import com.sjsu.cmpe272.prodwell.dto.TaskCompletionStats;
import com.sjsu.cmpe272.prodwell.entity.AIInsights;
import com.sjsu.cmpe272.prodwell.entity.Task;
import com.sjsu.cmpe272.prodwell.repository.AIInsightsRepository;
import com.sjsu.cmpe272.prodwell.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private AIInsightsRepository aiInsightsRepository;

    @InjectMocks
    private AnalyticsService service;

    @Test
    void shouldGetAnalyticsForHR() {
        List<Task> tasks = createTestTasks();
        Set<String> userIds = Set.of("user1", "user2");
        List<AIInsights> insights = createTestInsights();

        when(taskRepository.findAll()).thenReturn(tasks);
        when(aiInsightsRepository.findByOidIn(userIds)).thenReturn(insights);

        ManagerAnalyticsDTO result = service.getAnalytics(null, true);

        assertEquals("ALL", result.getProjectCode());
        assertEquals(2, result.getPeopleCount());
        assertNotNull(result.getTaskStats());
        assertNotNull(result.getTeamStressLevels());
    }

    @Test
    void shouldGetAnalyticsForProjectManager() {
        List<Task> tasks = createTestTasks();
        Set<String> userIds = Set.of("user1", "user2");
        List<AIInsights> insights = createTestInsights();

        when(taskRepository.findByProjectCode("P1")).thenReturn(tasks);
        when(aiInsightsRepository.findByOidIn(userIds)).thenReturn(insights);

        ManagerAnalyticsDTO result = service.getAnalytics("P1", false);

        assertEquals("P1", result.getProjectCode());
        assertEquals(2, result.getPeopleCount());
    }

    @Test
    void shouldCalculateTaskCompletionStats() {
        LocalDate today = LocalDate.now();
        List<Task> tasks = Arrays.asList(
                createTask(today.minusDays(1), today), // Before time
                createTask(today, today),              // On time
                createTask(today.plusDays(1), today)   // Late
        );

        when(taskRepository.findAll()).thenReturn(tasks);
        when(aiInsightsRepository.findByOidIn(any())).thenReturn(new ArrayList<>());

        ManagerAnalyticsDTO result = service.getAnalytics(null, true);
        TaskCompletionStats stats = result.getTaskStats();

        assertEquals(1, stats.getCompletedBeforeTime());
        assertEquals(1, stats.getCompletedOnTime());
        assertEquals(1, stats.getCompletedLate());
    }

    @Test
    void shouldHandleEmptyTasks() {
        when(taskRepository.findAll()).thenReturn(new ArrayList<>());
        when(aiInsightsRepository.findByOidIn(any())).thenReturn(new ArrayList<>());

        ManagerAnalyticsDTO result = service.getAnalytics(null, true);

        assertEquals(0, result.getPeopleCount());
        assertEquals(0, result.getTaskStats().getCompletedBeforeTime());
        assertTrue(result.getTeamStressLevels().isEmpty());
    }

    @Test
    void shouldHandleNullDates() {
        Task task = new Task();
        task.setUserId("user1");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task));
        when(aiInsightsRepository.findByOidIn(any())).thenReturn(new ArrayList<>());

        ManagerAnalyticsDTO result = service.getAnalytics(null, true);

        assertEquals(0, result.getTaskStats().getCompletedBeforeTime());
    }

    private List<Task> createTestTasks() {
        LocalDate today = LocalDate.now();
        Task task1 = createTask(today.minusDays(1), today);
        task1.setUserId("user1");
        Task task2 = createTask(today, today);
        task2.setUserId("user2");
        return Arrays.asList(task1, task2);
    }

    private Task createTask(LocalDate endDate, LocalDate deadlineDate) {
        Task task = new Task();
        task.setTaskEndDate(endDate);
        task.setDeadlineDate(deadlineDate);
        return task;
    }

    private List<AIInsights> createTestInsights() {
        AIInsights insight = new AIInsights();
        AIInsights.DailyStressLevel level = new AIInsights.DailyStressLevel();
        level.setDate("2024-01-01");
        level.setStressLevel(5.0);
        insight.setDailyStressLevels(Arrays.asList(level));
        return Arrays.asList(insight);
    }
}
