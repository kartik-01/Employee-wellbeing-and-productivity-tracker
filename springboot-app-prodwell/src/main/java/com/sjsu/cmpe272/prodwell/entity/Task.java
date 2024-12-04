package com.sjsu.cmpe272.prodwell.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sjsu.cmpe272.prodwell.config.DailyHoursConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Convert;

@Document(collection = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @JsonIgnore
    private ObjectId id;
    private String taskId;
    private String userId;
    private String taskName;
    private LocalDate assignedDate;
    private LocalDate deadlineDate;
    private LocalDate taskStartDate;
    private LocalDate taskEndDate;
    private String projectCode;
    @Field(name = "dailyHours")
    @Convert(converter = DailyHoursConverter.class)
    private List<Map<String, Integer>> dailyHours;
    public void generateTaskId() {
        if (this.taskId == null || this.taskId.isEmpty()) {
            this.taskId = UUID.randomUUID().toString();
        }
    }
}
