package com.sjsu.cmpe272.prodwell.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import lombok.Data;

@Data
public abstract class BaseSurveyQuestion {
    @Id
    private ObjectId id;
    private String question;
    private String type;
    private String[] options;
}
