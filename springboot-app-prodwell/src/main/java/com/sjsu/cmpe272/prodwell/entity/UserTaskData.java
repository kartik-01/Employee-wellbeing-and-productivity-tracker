package com.sjsu.cmpe272.prodwell.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "user_tasks")
public class UserTaskData {
    @Id
    private String id;
    private String userId;
    private String personality;
    private int taskAssigned;
    private int hoursSpent;
    private String submissionTime;
    private String hobbies;
    private int stressLevel;
    private String suggestion;
}
