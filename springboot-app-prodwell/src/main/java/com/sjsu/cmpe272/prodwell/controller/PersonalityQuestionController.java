package com.sjsu.cmpe272.prodwell.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.sjsu.cmpe272.prodwell.entity.PersonalityQuestion;
import com.sjsu.cmpe272.prodwell.service.PersonalityQuestionService;

//@CrossOrigin(origins = "${cors.allowed-origins}")
@CrossOrigin(origins = "https://prodwell-tracker.vercel.app")
@RestController
@RequestMapping("/personalityQuestions")
public class PersonalityQuestionController {

    @Autowired
    private PersonalityQuestionService service;

    @GetMapping("/")
    public List<PersonalityQuestion> getAll() {
        return service.getAll();
    }

    @GetMapping("/{questionId}")
    public PersonalityQuestion getByQuestionId(@PathVariable String questionId) {
        return service.getByQuestionId(questionId);
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

    @PutMapping("/{questionId}")
    public PersonalityQuestion update(@PathVariable String questionId, @RequestBody PersonalityQuestion question) {
        return service.update(questionId, question);
    }

    @DeleteMapping("/{questionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String questionId) {
        service.delete(questionId);
    }
}
