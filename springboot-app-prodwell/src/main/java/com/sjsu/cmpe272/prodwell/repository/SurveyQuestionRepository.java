package com.sjsu.cmpe272.prodwell.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.sjsu.cmpe272.prodwell.entity.SurveyQuestion;

public interface SurveyQuestionRepository extends MongoRepository<SurveyQuestion, String> {
	
	// Custom query to get all survey questions
    @Query("{}")
    List<SurveyQuestion> getAllSurveyQuestions();

    // Custom finder method to get survey questions by category
    List<SurveyQuestion> findByCategory(String category);

    // Custom query to get survey questions by questionId
    @Query("{questionId: ?0}")
    SurveyQuestion getSurveyQuestionById(int questionId);
}
