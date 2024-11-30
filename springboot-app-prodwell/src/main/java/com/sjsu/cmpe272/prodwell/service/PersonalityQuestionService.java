package com.sjsu.cmpe272.prodwell.service;

import java.util.List;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sjsu.cmpe272.prodwell.entity.PersonalityQuestion;
import com.sjsu.cmpe272.prodwell.repository.PersonalityQuestionRepository;

@Service
public class PersonalityQuestionService {

    @Autowired
    private PersonalityQuestionRepository repository;

    public List<PersonalityQuestion> getAll() {
        return repository.findAll();
    }

    public PersonalityQuestion getByQuestionId(UUID questionId) {
        return repository.findByQuestionId(questionId).orElse(null);
    }


    public PersonalityQuestion add(PersonalityQuestion question) {
        if (question.getQuestionId() == null) {
            question.setQuestionId(UUID.randomUUID());
        }
        return repository.save(question);
    }

    public List<PersonalityQuestion> addMultiple(List<PersonalityQuestion> questions) {
        questions.forEach(question -> {
            if (question.getQuestionId() == null) {
                question.setQuestionId(UUID.randomUUID());
            }
        });
        return repository.saveAll(questions);
    }

    public PersonalityQuestion update(UUID questionId, PersonalityQuestion updatedQuestion) {
        PersonalityQuestion existingQuestion = repository.findByQuestionId(questionId).orElseThrow(
                () -> new IllegalArgumentException("Question with ID " + questionId + " not found"));
        updatedQuestion.setId(existingQuestion.getId());
        updatedQuestion.setQuestionId(existingQuestion.getQuestionId());
        return repository.save(updatedQuestion);
    }

    public void delete(UUID questionId) {
        repository.deleteByQuestionId(questionId);
    }
}
