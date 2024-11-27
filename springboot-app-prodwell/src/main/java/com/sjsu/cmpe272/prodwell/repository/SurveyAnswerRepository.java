package com.sjsu.cmpe272.prodwell.repository;


import com.sjsu.cmpe272.prodwell.entity.SurveyAnswer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SurveyAnswerRepository extends MongoRepository<SurveyAnswer, ObjectId> {
    List<SurveyAnswer> findByUserId(ObjectId userId);
}
