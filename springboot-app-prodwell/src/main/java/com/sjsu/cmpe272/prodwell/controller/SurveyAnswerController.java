package com.sjsu.cmpe272.prodwell.controller;

import com.sjsu.cmpe272.prodwell.entity.SurveyAnswer;
import com.sjsu.cmpe272.prodwell.service.SurveyAnswerService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/surveyAnswers")
public class SurveyAnswerController {

    @Autowired
    private SurveyAnswerService surveyAnswerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SurveyAnswer recordAnswer(@RequestBody SurveyAnswer surveyAnswer) {
        return surveyAnswerService.saveAnswer(surveyAnswer);
    }

    @GetMapping("/user/{userId}")
    public List<SurveyAnswer> getAnswersByUserId(@PathVariable ObjectId userId) {
        return surveyAnswerService.getAnswersByUserId(userId);
    }
}
