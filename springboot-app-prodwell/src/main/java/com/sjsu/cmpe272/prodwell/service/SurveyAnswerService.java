package com.sjsu.cmpe272.prodwell.service;

import com.sjsu.cmpe272.prodwell.entity.SurveyAnswer;
import com.sjsu.cmpe272.prodwell.repository.SurveyAnswerRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SurveyAnswerService {

    @Autowired
    private SurveyAnswerRepository surveyAnswerRepository;

    public SurveyAnswer saveAnswer(SurveyAnswer surveyAnswer) {
        surveyAnswer.setDateTime(LocalDateTime.now());
        return surveyAnswerRepository.save(surveyAnswer);
    }

    public List<SurveyAnswer> getAnswersByUserId(ObjectId userId) {
        return surveyAnswerRepository.findByUserId(userId);
    }
}
