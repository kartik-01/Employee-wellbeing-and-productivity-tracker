package com.sjsu.cmpe272.prodwell.repository;

import com.sjsu.cmpe272.prodwell.entity.PersonalityAnswer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonalityAnswerRepository extends MongoRepository<PersonalityAnswer, String> {
}
