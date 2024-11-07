package com.sjsu.cmpe272.prodwell.controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sjsu.cmpe272.prodwell.entity.SurveyQuestion;
import com.sjsu.cmpe272.prodwell.service.SurveyQuestionService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/surveyQuestions")
public class SurveyQuestionController {

    @Autowired
    private SurveyQuestionService surveyQuestionService;

    // Endpoint to get all survey questions
    @GetMapping
    public List<SurveyQuestion> getAllSurveyQuestions() {
        return surveyQuestionService.getAllSurveyQuestions();
    }

    // Endpoint to get survey questions by category
    @GetMapping("/category/{category}")
    public List<SurveyQuestion> getSurveyQuestionsByCategory(@PathVariable String category) {
        return surveyQuestionService.getSurveyQuestionsByCategory(category);
    }

    // Endpoint to get a survey question by questionId
    @GetMapping("/{questionId}")
    public SurveyQuestion getSurveyQuestionById(@PathVariable ObjectId questionId) {
        return surveyQuestionService.getSurveyQuestionById(questionId);
    }

    // Endpoint to add a new survey question
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SurveyQuestion addSurveyQuestion(@RequestBody SurveyQuestion surveyQuestion) {
        return surveyQuestionService.addSurveyQuestion(surveyQuestion);
    }

    // Endpoint to update an existing survey question
    @PutMapping("/{id}")
    public SurveyQuestion updateSurveyQuestion(@PathVariable ObjectId id, @RequestBody SurveyQuestion surveyQuestion) {
        surveyQuestion.setId(id);
        return surveyQuestionService.updateSurveyQuestion(surveyQuestion);
    }

    // Endpoint to delete a survey question by ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSurveyQuestion(@PathVariable ObjectId id) {
        surveyQuestionService.deleteSurveyQuestion(id);
    }
}
