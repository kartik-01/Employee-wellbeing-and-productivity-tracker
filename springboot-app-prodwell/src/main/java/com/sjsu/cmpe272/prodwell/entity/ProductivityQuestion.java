package com.sjsu.cmpe272.prodwell.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Document(collection = "productivityQuestions")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductivityQuestion extends BaseSurveyQuestion {
//    private String productivityMetric;
}
