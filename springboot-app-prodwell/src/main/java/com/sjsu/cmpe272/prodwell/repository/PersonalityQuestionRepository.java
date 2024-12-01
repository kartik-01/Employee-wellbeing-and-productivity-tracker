package com.sjsu.cmpe272.prodwell.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.sjsu.cmpe272.prodwell.entity.PersonalityQuestion;

import java.util.Optional;

public interface PersonalityQuestionRepository extends MongoRepository<PersonalityQuestion, ObjectId> {
    Optional<PersonalityQuestion> findByQuestionId(String questionId); // Changed UUID to String

    void deleteByQuestionId(String questionId); // Changed UUID to String
}
