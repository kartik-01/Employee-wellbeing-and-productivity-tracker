package com.sjsu.cmpe272.prodwell.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class PersonalityAnswerTest {

    @Test
    void shouldCreateAnswerWithQuestions() {
        LocalDateTime now = LocalDateTime.now();
        PersonalityAnswer.QuestionAnswer qa = new PersonalityAnswer.QuestionAnswer("Q1", Arrays.asList("A1", "A2"));

        PersonalityAnswer answer = new PersonalityAnswer();
        answer.setUserId("user-1");
        answer.setAnswers(Arrays.asList(qa));
        answer.setDateTime(now);

        assertEquals("user-1", answer.getUserId());
        assertEquals(1, answer.getAnswers().size());
        assertEquals("Q1", answer.getAnswers().get(0).getQuestionId());
        assertEquals(now, answer.getDateTime());
    }

    @Test
    void shouldHandleEmptyAnswerList() {
        PersonalityAnswer answer = new PersonalityAnswer();
        assertNull(answer.getAnswers());
        assertNull(answer.getDateTime());
    }
}