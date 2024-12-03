package com.sjsu.cmpe272.prodwell.entity;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDataDTO {
    private User user;
    private List<PersonalityData> personalityData;
    private List<TaskData> tasks;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonalityData {
        private String question;
        private List<String> answer;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskData {
        private String taskName;
        private LocalDate assignedDate;
        private LocalDate deadlineDate;
        private LocalDate taskStartDate;
        private LocalDate taskEndDate;
        private int totalNoHours;
    }

    // Custom constructor for partial data
    public UserDataDTO(User user) {
        this.user = user;
    }
}