package com.sjsu.cmpe272.prodwell.service;

import com.sjsu.cmpe272.prodwell.entity.PersonalityAnswer;
import com.sjsu.cmpe272.prodwell.repository.PersonalityAnswerRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        // Check if the record already exists for the given userId
        Optional<PersonalityAnswer> existingAnswerOptional = repository.findByOid(newAnswer.getOid());

        if (existingAnswerOptional.isPresent()) {
            // Update existing record
            PersonalityAnswer existingAnswer = existingAnswerOptional.get();
            List<PersonalityAnswer.QuestionAnswer> updatedAnswers = newAnswer.getAnswers();

            // Update each question-answer pair in the existing record
            for (PersonalityAnswer.QuestionAnswer updatedQA : updatedAnswers) {
                boolean questionExists = false;

                for (PersonalityAnswer.QuestionAnswer existingQA : existingAnswer.getAnswers()) {
                    if (existingQA.getQuestionId().equals(updatedQA.getQuestionId())) {
                        // Update the existing question's answers
                        existingQA.setAnswer(updatedQA.getAnswer());
                        questionExists = true;
                        break;
                    }
                }

                // If the question does not exist, add it as a new entry
                if (!questionExists) {
                    existingAnswer.getAnswers().add(updatedQA);
                }
            }

            existingAnswer.setDateTime(LocalDateTime.now()); // Update timestamp
            return repository.save(existingAnswer);
        } else {
            // Create a new record
            newAnswer.setDateTime(LocalDateTime.now());
            return repository.save(newAnswer);
        }
    }

    public Optional<PersonalityAnswer> getAnswerByUserId(String oid) {
        return repository.findByOid(oid);
    }

    public void deleteAnswerByUserId(String oid) {
        repository.deleteByOid(oid);
    }

    // Get all answers (Optional)
    public List<PersonalityAnswer> getAllAnswers() {
        return repository.findAll();
    }
}
