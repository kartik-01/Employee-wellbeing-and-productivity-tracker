package com.sjsu.cmpe272.prodwell.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "productivityAnswers")
public class ProductivityAnswer extends BaseSurveyAnswer {
}