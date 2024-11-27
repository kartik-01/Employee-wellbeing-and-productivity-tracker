package com.sjsu.cmpe272.prodwell.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.sjsu.cmpe272.prodwell.entity.PersonalityQuestion;

public interface PersonalityQuestionRepository extends MongoRepository<PersonalityQuestion, ObjectId> {
}
