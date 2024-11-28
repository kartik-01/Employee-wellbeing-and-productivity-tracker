package com.sjsu.cmpe272.prodwell.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    private ObjectId id;
    private ObjectId userId;
    private String taskName;
    private LocalDate assignedDate;
    private LocalDate deadlineDate;
    private LocalDate taskStartDate;
    private LocalDate taskEndDate;
    private int totalNoHours;
}
