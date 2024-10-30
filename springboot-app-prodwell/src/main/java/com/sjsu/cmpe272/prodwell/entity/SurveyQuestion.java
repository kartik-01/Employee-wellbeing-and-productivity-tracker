package com.sjsu.cmpe272.prodwell.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "surveyQuestions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyQuestion {

    @Id
    private String id;
    private int questionId;
    private String category;
    private String question;
}