package com.sjsu.cmpe272.prodwell.controller;

import com.sjsu.cmpe272.prodwell.entity.PersonalityAnswer;
import com.sjsu.cmpe272.prodwell.service.PersonalityAnswerService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/personalityAnswers")
public class PersonalityAnswerController {

    @Autowired
    private PersonalityAnswerService service;

    @PostMapping("/")
    public ResponseEntity<PersonalityAnswer> saveOrUpdateAnswer(@RequestBody PersonalityAnswer personalityAnswer) {
        PersonalityAnswer savedAnswer = service.saveOrUpdateAnswer(personalityAnswer);
        return ResponseEntity.ok(savedAnswer);
    }

    // Get PersonalityAnswer by userId
    @GetMapping("/{userId}")
    public ResponseEntity<PersonalityAnswer> getAnswerByUserId(@PathVariable String userId) {
        Optional<PersonalityAnswer> answer = service.getAnswerByUserId(userId);
        return answer.map(ResponseEntity::ok) // If present, return 200 OK with the answer
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null)); // If not found, return 404 Not Found
    }

    // Delete PersonalityAnswer by userId
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteAnswerByUserId(@PathVariable String userId) {
        service.deleteAnswerByUserId(userId);
        return ResponseEntity.noContent().build();
    }

    // Get all PersonalityAnswers (Optional if needed)
    @GetMapping("/")
    public List<PersonalityAnswer> getAllAnswers() {
        return service.getAllAnswers();
    }
}
