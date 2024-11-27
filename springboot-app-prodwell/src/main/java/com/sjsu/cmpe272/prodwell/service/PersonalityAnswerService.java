package com.sjsu.cmpe272.prodwell.service;

import com.sjsu.cmpe272.prodwell.entity.PersonalityAnswer;
import com.sjsu.cmpe272.prodwell.repository.PersonalityAnswerRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PersonalityAnswerService {

    @Autowired
    private PersonalityAnswerRepository repository;

    public PersonalityAnswer saveAnswer(PersonalityAnswer answer) {
        answer.setDateTime(LocalDateTime.now());
        return repository.save(answer);
    }

    public List<PersonalityAnswer> getAnswersByUserId(ObjectId userId) {
        return repository.findByUserId(userId);
    }
}
