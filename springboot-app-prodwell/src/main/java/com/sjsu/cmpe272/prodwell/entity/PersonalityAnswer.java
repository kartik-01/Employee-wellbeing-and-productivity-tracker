package com.sjsu.cmpe272.prodwell.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "personalityAnswers")
public class PersonalityAnswer extends BaseSurveyAnswer {
}