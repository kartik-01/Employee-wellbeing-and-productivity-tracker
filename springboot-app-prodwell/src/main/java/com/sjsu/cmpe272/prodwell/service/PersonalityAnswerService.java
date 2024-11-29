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

    // Save a new answer
    public PersonalityAnswer saveAnswer(PersonalityAnswer answer) {
        answer.setDateTime(LocalDateTime.now());
        return repository.save(answer);
    }

    // Get all answers by user ID
    public List<PersonalityAnswer> getAnswersByUserId(ObjectId userId) {
        return repository.findByUserId(userId);
    }

    // Get a specific answer by its ID
    public PersonalityAnswer getAnswerById(ObjectId id) {
        return repository.findById(id).orElse(null);
    }

    // Update an existing answer
    public PersonalityAnswer updateAnswer(PersonalityAnswer answer) {
        answer.setDateTime(LocalDateTime.now()); // Update timestamp
        return repository.save(answer);
    }

    // Delete an answer by its ID
    public void deleteAnswer(ObjectId id) {
        repository.deleteById(id);
    }

    // Get all answers (Optional)
    public List<PersonalityAnswer> getAllAnswers() {
        return repository.findAll();
    }
}
