package com.sjsu.cmpe272.prodwell.entity;

import org.junit.jupiter.api.Test;
import org.bson.types.ObjectId;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class PersonalityQuestionTest {

    @Test
    void shouldCreateQuestionWithAllFields() {
        PersonalityQuestion question = new PersonalityQuestion();
        question.setId(new ObjectId());
        question.setQuestionId("Q1");
        question.setQuestion("Test question?");
        question.setType("MULTIPLE_CHOICE");
        question.setOptions(Arrays.asList("Option 1", "Option 2"));

        assertNotNull(question.getId());
        assertEquals("Q1", question.getQuestionId());
        assertEquals("Test question?", question.getQuestion());
        assertEquals("MULTIPLE_CHOICE", question.getType());
        assertEquals(2, question.getOptions().size());
    }

    @Test
    void shouldHandleNullFields() {
        PersonalityQuestion question = new PersonalityQuestion();
        assertNull(question.getId());
        assertNull(question.getQuestionId());
        assertNull(question.getOptions());
    }
}