package com.sjsu.cmpe272.prodwell.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String userId;
    private List<QuestionAnswer> answers;
    private LocalDateTime dateTime; // Timestamp for last update
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionAnswer {
        private String questionId;
        private List<String> answer;
    }
}