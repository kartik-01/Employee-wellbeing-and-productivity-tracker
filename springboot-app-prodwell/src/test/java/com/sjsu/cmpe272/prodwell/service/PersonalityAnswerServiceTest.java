package com.sjsu.cmpe272.prodwell.service;

import com.sjsu.cmpe272.prodwell.entity.PersonalityAnswer;
import com.sjsu.cmpe272.prodwell.repository.PersonalityAnswerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalityAnswerServiceTest {

    @Mock
    private PersonalityAnswerRepository repository;

    @InjectMocks
    private PersonalityAnswerService service;

    @Test
    void shouldSaveNewAnswer() {
        PersonalityAnswer answer = new PersonalityAnswer();
        answer.setUserId("test-user");

        when(repository.save(any(PersonalityAnswer.class))).thenReturn(answer);

        PersonalityAnswer result = service.saveAnswer(answer);
        assertNotNull(result);
        assertNotNull(result.getDateTime());
    }

    @Test
    void shouldUpdateExistingAnswer() {
        PersonalityAnswer existingAnswer = new PersonalityAnswer();
        existingAnswer.setUserId("test-user");
        existingAnswer.setAnswers(new ArrayList<>());

        PersonalityAnswer newAnswer = new PersonalityAnswer();
        newAnswer.setUserId("test-user");
        newAnswer.setAnswers(Arrays.asList(
                new PersonalityAnswer.QuestionAnswer("Q1", Arrays.asList("A1"))
        ));

        when(repository.findById("test-user")).thenReturn(Optional.of(existingAnswer));
        when(repository.save(any(PersonalityAnswer.class))).thenReturn(existingAnswer);

        PersonalityAnswer result = service.saveOrUpdateAnswer(newAnswer);
        assertNotNull(result);
        assertFalse(result.getAnswers().isEmpty());
    }

    @Test
    void shouldGetAllAnswers() {
        List<PersonalityAnswer> answers = Arrays.asList(new PersonalityAnswer());
        when(repository.findAll()).thenReturn(answers);

        List<PersonalityAnswer> result = service.getAllAnswers();
        assertFalse(result.isEmpty());
    }

    @Test
    void shouldDeleteAnswerByUserId() {
        doNothing().when(repository).deleteById("test-user");
        service.deleteAnswerByUserId("test-user");
        verify(repository).deleteById("test-user");
    }
}