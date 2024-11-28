package com.sjsu.cmpe272.prodwell.controller;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sjsu.cmpe272.prodwell.entity.PersonalityQuestion;
import com.sjsu.cmpe272.prodwell.service.PersonalityQuestionService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/personalityQuestions")
public class PersonalityQuestionController {

    @Autowired
    private PersonalityQuestionService service;

    @GetMapping("/")
    public List<PersonalityQuestion> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public PersonalityQuestion getById(@PathVariable ObjectId id) {
        return service.getById(id);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public PersonalityQuestion add(@RequestBody PersonalityQuestion question) {
        return service.add(question);
    }

    @PostMapping("/bulk")
    public List<PersonalityQuestion> addMultipleQuestions(@RequestBody List<PersonalityQuestion> questions) {
        return service.addMultiple(questions);
    }

    @PutMapping("/{id}")
    public PersonalityQuestion update(@PathVariable ObjectId id, @RequestBody PersonalityQuestion question) {
        question.setId(id);
        return service.update(question);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable ObjectId id) {
        service.delete(id);
    }
}
