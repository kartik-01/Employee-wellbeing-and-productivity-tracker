package com.sjsu.cmpe272.prodwell.entity;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "personalityAnswers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalityAnswer{
    @Id
    private ObjectId id;
    private ObjectId questionId;
    private ObjectId userId;
    private String answer;
    private LocalDateTime dateTime;
}