package com.sjsu.cmpe272.prodwell.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Document(collection = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @JsonIgnore
    private ObjectId id;
    private String taskId;
    private String oid;
    private String taskName;
    private LocalDate assignedDate;
    private LocalDate deadlineDate;
    private LocalDate taskStartDate;
    private LocalDate taskEndDate;
    private int totalNoHours;

    public void generateTaskId() {
        if (this.taskId == null) {
            this.taskId = UUID.randomUUID().toString();
            this.taskId = UUID.randomUUID().toString();
        }
    }
}
