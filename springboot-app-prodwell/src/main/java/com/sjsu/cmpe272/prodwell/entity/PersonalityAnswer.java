package com.sjsu.cmpe272.prodwell.entity;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "personalityAnswers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalityAnswer {
    @Id
    private String userId;
    
    @Field
    private List<QuestionAnswer> answers;
    
    private LocalDateTime dateTime;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionAnswer {
        private String questionId;
        private List<String> answer;
    }
}