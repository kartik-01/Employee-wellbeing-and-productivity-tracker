package com.sjsu.cmpe272.prodwell.service;

import com.sjsu.cmpe272.prodwell.entity.PersonalityQuestion;
import com.sjsu.cmpe272.prodwell.repository.PersonalityQuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalityQuestionServiceTest {

    @Mock
    private PersonalityQuestionRepository repository;

    @InjectMocks
    private PersonalityQuestionService service;

    @Test
    void shouldGetAllQuestions() {
        PersonalityQuestion question = new PersonalityQuestion();
        when(repository.findAll()).thenReturn(Arrays.asList(question));

        List<PersonalityQuestion> result = service.getAll();
        assertFalse(result.isEmpty());
    }

    @Test
    void shouldAddQuestion() {
        PersonalityQuestion question = new PersonalityQuestion();
        when(repository.save(any(PersonalityQuestion.class))).thenReturn(question);

        PersonalityQuestion result = service.add(question);
        assertNotNull(result);
        assertNotNull(result.getQuestionId());
    }

    @Test
    void shouldAddMultipleQuestions() {
        List<PersonalityQuestion> questions = Arrays.asList(
                new PersonalityQuestion(),
                new PersonalityQuestion()
        );
        when(repository.saveAll(anyList())).thenReturn(questions);

        List<PersonalityQuestion> results = service.addMultiple(questions);
        assertEquals(2, results.size());
        assertNotNull(results.get(0).getQuestionId());
    }

    @Test
    void shouldUpdateQuestion() {
        String questionId = "Q1";
        PersonalityQuestion existingQuestion = new PersonalityQuestion();
        existingQuestion.setQuestionId(questionId);

        PersonalityQuestion updatedQuestion = new PersonalityQuestion();
        updatedQuestion.setQuestion("Updated Question");

        when(repository.findByQuestionId(questionId)).thenReturn(Optional.of(existingQuestion));
        when(repository.save(any(PersonalityQuestion.class))).thenReturn(updatedQuestion);

        PersonalityQuestion result = service.update(questionId, updatedQuestion);
        assertEquals("Updated Question", result.getQuestion());
    }

    @Test
    void shouldThrowExceptionWhenQuestionNotFound() {
        PersonalityQuestion question = new PersonalityQuestion();
        assertThrows(IllegalArgumentException.class,
                () -> service.update("non-existent", question));
    }
}