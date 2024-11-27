package com.sjsu.cmpe272.prodwell.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Document(collection = "personalityQuestions")
@Data
@EqualsAndHashCode(callSuper = true)
public class PersonalityQuestion extends BaseSurveyQuestion {
//    private String personalityTrait;
}
