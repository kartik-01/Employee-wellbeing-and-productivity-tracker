package com.sjsu.cmpe272.prodwell.repository;

import com.sjsu.cmpe272.prodwell.entity.PersonalityAnswer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PersonalityAnswerRepository extends MongoRepository<PersonalityAnswer, ObjectId> {
    List<PersonalityAnswer> findByUserId(ObjectId userId);
}