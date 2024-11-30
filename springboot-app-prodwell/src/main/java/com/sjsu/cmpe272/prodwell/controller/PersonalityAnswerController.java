package com.sjsu.cmpe272.prodwell.controller;

import com.sjsu.cmpe272.prodwell.entity.PersonalityAnswer;
import com.sjsu.cmpe272.prodwell.service.PersonalityAnswerService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/personalityAnswers")
public class PersonalityAnswerController {

    @Autowired
    private PersonalityAnswerService service;

    // Create a new PersonalityAnswer
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public PersonalityAnswer recordAnswer(@RequestBody PersonalityAnswer answer) {
        return service.saveAnswer(answer);
    }

    // Get all answers for a specific user
    @GetMapping("/user/{userId}")
    public List<PersonalityAnswer> getAnswersByUserId(@PathVariable ObjectId userId) {
        return service.getAnswersByUserId(userId);
    }

    // Get a specific PersonalityAnswer by its ID
    @GetMapping("/{answerId}")
    public PersonalityAnswer getAnswerById(@PathVariable ObjectId answerId) {
        return service.getAnswerById(answerId);
    }

    // Update an existing PersonalityAnswer by its ID
    @PutMapping("/{answerId}")
    public PersonalityAnswer updateAnswer(@PathVariable ObjectId answerId, @RequestBody PersonalityAnswer answer) {
        answer.setId(answerId);
        return service.updateAnswer(answer);
    }

    // Delete a PersonalityAnswer by its ID
    @DeleteMapping("/{answerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAnswer(@PathVariable ObjectId answerId) {
        service.deleteAnswer(answerId);
    }

    // Get all PersonalityAnswers (Optional if needed)
    @GetMapping("/")
    public List<PersonalityAnswer> getAllAnswers() {
        return service.getAllAnswers();
    }
}
