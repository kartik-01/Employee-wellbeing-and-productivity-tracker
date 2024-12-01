package com.sjsu.cmpe272.prodwell.controller;

import com.sjsu.cmpe272.prodwell.entity.PersonalityAnswer;
import com.sjsu.cmpe272.prodwell.service.PersonalityAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @GetMapping("/{oid}")
    public ResponseEntity<PersonalityAnswer> getAnswerByUserId(@PathVariable String oid) {
        Optional<PersonalityAnswer> answer = service.getAnswerByUserId(oid);
        return answer.map(ResponseEntity::ok) // If present, return 200 OK with the answer
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null)); // If not found, return 404 Not Found
    }

    // Delete PersonalityAnswer by userId
    @DeleteMapping("/{oid}")
    public ResponseEntity<Void> deleteAnswerByUserId(@PathVariable String oid) {
        service.deleteAnswerByUserId(oid);
        return ResponseEntity.noContent().build();
    }

    // Get all PersonalityAnswers (Optional if needed)
    @GetMapping("/")
    public List<PersonalityAnswer> getAllAnswers() {
        return service.getAllAnswers();
    }
}
