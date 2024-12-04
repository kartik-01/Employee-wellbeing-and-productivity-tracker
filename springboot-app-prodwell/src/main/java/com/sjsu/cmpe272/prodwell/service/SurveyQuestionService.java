package com.sjsu.cmpe272.prodwell.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sjsu.cmpe272.prodwell.entity.SurveyQuestion;
import com.sjsu.cmpe272.prodwell.repository.SurveyQuestionRepository;

@Service
public class SurveyQuestionService {

    @Autowired
    private SurveyQuestionRepository surveyQuestionRepository;

    // Method to get all survey questions
    public List<SurveyQuestion> getAllSurveyQuestions() {
        return surveyQuestionRepository.findAll();
    }

    // Method to get survey questions by category
    public List<SurveyQuestion> getSurveyQuestionsByCategory(String category) {
        return surveyQuestionRepository.findByCategory(category);
    }

    // Method to get a survey question by questionId
    public SurveyQuestion getSurveyQuestionById(int questionId) {
        return surveyQuestionRepository.getSurveyQuestionById(questionId);
    }

    // Method to add a new survey question
    public SurveyQuestion addSurveyQuestion(SurveyQuestion surveyQuestion) {
        // Assign a unique ID to the survey question if not already set
        if (surveyQuestion.getId() == null) {
            surveyQuestion.setId(UUID.randomUUID().toString());
        }
        return surveyQuestionRepository.save(surveyQuestion);
    }

    // Method to update an existing survey question
    public SurveyQuestion updateSurveyQuestion(SurveyQuestion surveyQuestion) {
        return surveyQuestionRepository.save(surveyQuestion);
    }

    // Method to delete a survey question by ID
    public void deleteSurveyQuestion(String id) {
        surveyQuestionRepository.deleteById(id);
    }
}
