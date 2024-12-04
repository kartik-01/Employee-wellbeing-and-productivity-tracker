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
        verify(repository).save(any(PersonalityAnswer.class));
    }

    @Test
    void shouldUpdateExistingAnswerWithNewQuestions() {
        // Setup existing answer
        PersonalityAnswer existingAnswer = new PersonalityAnswer();
        existingAnswer.setUserId("test-user");
        existingAnswer.setAnswers(new ArrayList<>(Arrays.asList(
                new PersonalityAnswer.QuestionAnswer("Q1", Arrays.asList("A1"))
        )));

        // Setup new answer with different question
        PersonalityAnswer newAnswer = new PersonalityAnswer();
        newAnswer.setUserId("test-user");
        newAnswer.setAnswers(Arrays.asList(
                new PersonalityAnswer.QuestionAnswer("Q2", Arrays.asList("A2"))
        ));

        when(repository.findById("test-user")).thenReturn(Optional.of(existingAnswer));
        when(repository.save(any(PersonalityAnswer.class))).thenReturn(existingAnswer);

        PersonalityAnswer result = service.saveOrUpdateAnswer(newAnswer);
        assertEquals(2, result.getAnswers().size());
    }

    @Test
    void shouldUpdateExistingAnswerWithSameQuestion() {
        // Setup existing answer
        PersonalityAnswer existingAnswer = new PersonalityAnswer();
        existingAnswer.setUserId("test-user");
        existingAnswer.setAnswers(new ArrayList<>(Arrays.asList(
                new PersonalityAnswer.QuestionAnswer("Q1", Arrays.asList("A1"))
        )));

        // Setup new answer with same question but different answer
        PersonalityAnswer newAnswer = new PersonalityAnswer();
        newAnswer.setUserId("test-user");
        newAnswer.setAnswers(Arrays.asList(
                new PersonalityAnswer.QuestionAnswer("Q1", Arrays.asList("A2"))
        ));

        when(repository.findById("test-user")).thenReturn(Optional.of(existingAnswer));
        when(repository.save(any(PersonalityAnswer.class))).thenReturn(existingAnswer);

        PersonalityAnswer result = service.saveOrUpdateAnswer(newAnswer);
        assertEquals(1, result.getAnswers().size());
        assertEquals("A2", result.getAnswers().get(0).getAnswer().get(0));
    }

    @Test
    void shouldSaveNewAnswerWhenNoExisting() {
        PersonalityAnswer newAnswer = new PersonalityAnswer();
        newAnswer.setUserId("new-user");
        newAnswer.setAnswers(Arrays.asList(
                new PersonalityAnswer.QuestionAnswer("Q1", Arrays.asList("A1"))
        ));

        when(repository.findById("new-user")).thenReturn(Optional.empty());
        when(repository.save(any(PersonalityAnswer.class))).thenReturn(newAnswer);

        PersonalityAnswer result = service.saveOrUpdateAnswer(newAnswer);
        assertNotNull(result.getDateTime());
        assertEquals(1, result.getAnswers().size());
    }

    @Test
    void shouldHandleEmptyAnswersList() {
        PersonalityAnswer answer = new PersonalityAnswer();
        answer.setUserId("test-user");
        answer.setAnswers(new ArrayList<>());

        when(repository.save(any(PersonalityAnswer.class))).thenReturn(answer);

        PersonalityAnswer result = service.saveAnswer(answer);
        assertTrue(result.getAnswers().isEmpty());
    }

    @Test
    void shouldGetAnswerByUserId() {
        PersonalityAnswer answer = new PersonalityAnswer();
        when(repository.findById("test-user")).thenReturn(Optional.of(answer));

        Optional<PersonalityAnswer> result = service.getAnswerByUserId("test-user");
        assertTrue(result.isPresent());
    }

    @Test
    void shouldReturnEmptyOptionalForNonExistentUser() {
        when(repository.findById("non-existent")).thenReturn(Optional.empty());

        Optional<PersonalityAnswer> result = service.getAnswerByUserId("non-existent");
        assertFalse(result.isPresent());
    }

    @Test
    void shouldGetAllAnswersWhenEmpty() {
        when(repository.findAll()).thenReturn(new ArrayList<>());

        List<PersonalityAnswer> result = service.getAllAnswers();
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldHandleDeleteNonExistentAnswer() {
        doThrow(new RuntimeException("Not found")).when(repository).deleteById("non-existent");

        assertThrows(RuntimeException.class, () ->
                service.deleteAnswerByUserId("non-existent"));
    }
}