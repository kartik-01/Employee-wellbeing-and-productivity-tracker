package com.sjsu.cmpe272.prodwell.service;

import java.util.List;
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

    public PersonalityQuestion getById(ObjectId id) {
        return repository.findById(id).orElse(null);
    }

    public PersonalityQuestion add(PersonalityQuestion question) {
        return repository.save(question);
    }

    public PersonalityQuestion update(PersonalityQuestion question) {
        return repository.save(question);
    }

    public void delete(ObjectId id) {
        repository.deleteById(id);
    }
}
