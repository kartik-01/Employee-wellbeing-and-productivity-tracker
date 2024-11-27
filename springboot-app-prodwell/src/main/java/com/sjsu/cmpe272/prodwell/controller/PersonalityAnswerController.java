package com.sjsu.cmpe272.prodwell.controller;

import com.sjsu.cmpe272.prodwell.entity.PersonalityAnswer;
import com.sjsu.cmpe272.prodwell.service.PersonalityAnswerService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personalityAnswers")
public class PersonalityAnswerController {

    @Autowired
    private PersonalityAnswerService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonalityAnswer recordAnswer(@RequestBody PersonalityAnswer answer) {
        return service.saveAnswer(answer);
    }

    @GetMapping("/user/{userId}")
    public List<PersonalityAnswer> getAnswersByUserId(@PathVariable ObjectId userId) {
        return service.getAnswersByUserId(userId);
    }
}
