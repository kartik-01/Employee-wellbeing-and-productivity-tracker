package com.sjsu.cmpe272.prodwell.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class UserDataDTOTest {

    @Test
    void shouldCreateDTOWithAllData() {
        User user = new User();
        user.setOid("user-1");

        UserDataDTO.PersonalityData personalityData =
                new UserDataDTO.PersonalityData("Question?", Arrays.asList("Answer"));

        Map<String, Integer> hours = new HashMap<>();
        hours.put("2024-01-01", 8);
        UserDataDTO.TaskData taskData = new UserDataDTO.TaskData(
                "Task 1",
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                Arrays.asList(hours)
        );

        UserDataDTO dto = new UserDataDTO(user);
        dto.setPersonalityData(Arrays.asList(personalityData));
        dto.setTasks(Arrays.asList(taskData));

        assertNotNull(dto.getUser());
        assertEquals(1, dto.getPersonalityData().size());
        assertEquals(1, dto.getTasks().size());
    }

    @Test
    void shouldHandleNullFields() {
        UserDataDTO dto = new UserDataDTO();
        assertNull(dto.getUser());
        assertNull(dto.getPersonalityData());
        assertNull(dto.getTasks());
    }
}