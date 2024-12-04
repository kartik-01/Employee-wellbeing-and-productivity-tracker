package com.sjsu.cmpe272.prodwell.service;

import com.sjsu.cmpe272.prodwell.entity.PersonalityAnswer;
import com.sjsu.cmpe272.prodwell.repository.PersonalityAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PersonalityAnswerService {

    @Autowired
    private PersonalityAnswerRepository repository;

    // Save a new answer
    public PersonalityAnswer saveAnswer(PersonalityAnswer answer) {
        answer.setDateTime(LocalDateTime.now());
        return repository.save(answer);
    }

    public PersonalityAnswer saveOrUpdateAnswer(PersonalityAnswer newAnswer) {
        // Fetch existing answer for the userId
        Optional<PersonalityAnswer> existingAnswerOptional = repository.findById(newAnswer.getUserId());
    
        if (existingAnswerOptional.isPresent()) {
            // If existing, update the fields
            PersonalityAnswer existingAnswer = existingAnswerOptional.get();
            existingAnswer.setDateTime(LocalDateTime.now()); // Update timestamp
            
            // Update or add question-answers
            for (PersonalityAnswer.QuestionAnswer newQA : newAnswer.getAnswers()) {
                boolean isUpdated = false;
    
                for (PersonalityAnswer.QuestionAnswer existingQA : existingAnswer.getAnswers()) {
                    if (existingQA.getQuestionId().equals(newQA.getQuestionId())) {
                        existingQA.setAnswer(newQA.getAnswer());
                        isUpdated = true;
                        break;
                    }
                }
    
                if (!isUpdated) {
                    // Add new question-answer pair if it doesn't exist
                    existingAnswer.getAnswers().add(newQA);
                }
            }
    
            // Save the updated record
            return repository.save(existingAnswer);
        } else {
            // If no existing record, save as new
            newAnswer.setDateTime(LocalDateTime.now());
            return repository.save(newAnswer);
        }
    }    
    public Optional<PersonalityAnswer> getAnswerByUserId(String userId) {
        return repository.findById(userId);
    }

    public void deleteAnswerByUserId(String userId) {
        repository.deleteById(userId);
    }

    // Get all answers (Optional)
    public List<PersonalityAnswer> getAllAnswers() {
        return repository.findAll();
    }
}
