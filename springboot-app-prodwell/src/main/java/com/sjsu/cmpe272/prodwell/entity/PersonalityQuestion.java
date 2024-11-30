package com.sjsu.cmpe272.prodwell.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Document(collection = "personalityQuestions")
@Data
public class PersonalityQuestion {
    @Id
    @JsonIgnore
    private ObjectId id;
    private UUID questionId;
    private String question;
    private String type;
    private List<String> options;
}
