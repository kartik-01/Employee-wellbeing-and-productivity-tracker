package com.sjsu.cmpe272.prodwell.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.sjsu.cmpe272.prodwell.entity.PersonalityQuestion;

import java.util.Optional;
import java.util.UUID;

public interface PersonalityQuestionRepository extends MongoRepository<PersonalityQuestion, ObjectId> {
    Optional<PersonalityQuestion> findByQuestionId(UUID questionId);

    void deleteByQuestionId(UUID questionId);
}
