package com.sjsu.cmpe272.prodwell.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sjsu.cmpe272.prodwell.entity.PersonalityQuestion;
import com.sjsu.cmpe272.prodwell.service.PersonalityQuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonalityQuestionController.class)
@AutoConfigureMockMvc(addFilters = false)
class PersonalityQuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonalityQuestionService service;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCreateQuestion() throws Exception {
        PersonalityQuestion question = new PersonalityQuestion();
        question.setQuestionId("Q1");
        question.setQuestion("Test Question");
        question.setType("MULTIPLE_CHOICE");
        question.setOptions(Arrays.asList("Option 1", "Option 2"));

        when(service.add(any(PersonalityQuestion.class))).thenReturn(question);

        mockMvc.perform(post("/personalityQuestions/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.questionId").value("Q1"))
                .andExpect(jsonPath("$.question").value("Test Question"));
    }

    @Test
    void shouldGetAllQuestions() throws Exception {
        PersonalityQuestion question1 = new PersonalityQuestion();
        question1.setQuestionId("Q1");
        PersonalityQuestion question2 = new PersonalityQuestion();
        question2.setQuestionId("Q2");

        when(service.getAll()).thenReturn(Arrays.asList(question1, question2));

        mockMvc.perform(get("/personalityQuestions/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionId").value("Q1"))
                .andExpect(jsonPath("$[1].questionId").value("Q2"));
    }

    @Test
    void shouldGetQuestionById() throws Exception {
        PersonalityQuestion question = new PersonalityQuestion();
        question.setQuestionId("Q1");
        question.setQuestion("Test Question");

        when(service.getByQuestionId("Q1")).thenReturn(question);

        mockMvc.perform(get("/personalityQuestions/Q1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionId").value("Q1"))
                .andExpect(jsonPath("$.question").value("Test Question"));
    }

    @Test
    void shouldCreateBulkQuestions() throws Exception {
        List<PersonalityQuestion> questions = Arrays.asList(
                new PersonalityQuestion(),
                new PersonalityQuestion()
        );
        questions.get(0).setQuestionId("Q1");
        questions.get(1).setQuestionId("Q2");

        when(service.addMultiple(any())).thenReturn(questions);

        mockMvc.perform(post("/personalityQuestions/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questions)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionId").value("Q1"))
                .andExpect(jsonPath("$[1].questionId").value("Q2"));
    }

    @Test
    void shouldUpdateQuestion() throws Exception {
        PersonalityQuestion question = new PersonalityQuestion();
        question.setQuestionId("Q1");
        question.setQuestion("Updated Question");

        when(service.update(eq("Q1"), any(PersonalityQuestion.class))).thenReturn(question);

        mockMvc.perform(put("/personalityQuestions/Q1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionId").value("Q1"))
                .andExpect(jsonPath("$.question").value("Updated Question"));
    }

    @Test
    void shouldDeleteQuestion() throws Exception {
        doNothing().when(service).delete("Q1");

        mockMvc.perform(delete("/personalityQuestions/Q1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete("Q1");
    }
}