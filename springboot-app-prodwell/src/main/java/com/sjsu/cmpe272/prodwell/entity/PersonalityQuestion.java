package com.sjsu.cmpe272.prodwell.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.util.List;

@Document(collection = "personalityQuestions")
@Data
public class PersonalityQuestion {
    @Id
    @JsonIgnore
    private ObjectId id;

    private String questionId; // Changed from UUID to String
    private String question;
    private String type;
    private List<String> options;
}
