package com.sjsu.cmpe272.prodwell.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseSurveyAnswer {
    @Id
    private ObjectId id;
    private ObjectId questionId;
    private ObjectId userId;
    private String answer;
    private LocalDateTime dateTime;
}
