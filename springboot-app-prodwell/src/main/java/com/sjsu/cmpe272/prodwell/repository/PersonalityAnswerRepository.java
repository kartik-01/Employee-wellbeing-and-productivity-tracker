package com.sjsu.cmpe272.prodwell.repository;

import com.sjsu.cmpe272.prodwell.entity.PersonalityAnswer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PersonalityAnswerRepository extends MongoRepository<PersonalityAnswer, String> {
    Optional<PersonalityAnswer> findByOid(String oid);

    void deleteByOid(String oid);
}
