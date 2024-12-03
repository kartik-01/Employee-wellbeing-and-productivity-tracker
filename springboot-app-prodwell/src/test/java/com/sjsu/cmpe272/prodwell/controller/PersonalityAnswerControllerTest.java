package com.sjsu.cmpe272.prodwell.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sjsu.cmpe272.prodwell.entity.PersonalityAnswer;
import com.sjsu.cmpe272.prodwell.service.PersonalityAnswerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonalityAnswerController.class)
@AutoConfigureMockMvc(addFilters = false)  // This will disable security filters
class PersonalityAnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonalityAnswerService service;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldSavePersonalityAnswer() throws Exception {
        PersonalityAnswer answer = new PersonalityAnswer();
        answer.setUserId("test-user");
        answer.setDateTime(LocalDateTime.now());
        answer.setAnswers(Arrays.asList(
                new PersonalityAnswer.QuestionAnswer("Q1", Arrays.asList("A1"))
        ));

        when(service.saveOrUpdateAnswer(any(PersonalityAnswer.class))).thenReturn(answer);

        mockMvc.perform(post("/personalityAnswers/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("test-user"));
    }

    @Test
    void shouldGetAnswerByUserId() throws Exception {
        PersonalityAnswer answer = new PersonalityAnswer();
        answer.setUserId("test-user");
        answer.setDateTime(LocalDateTime.now());

        when(service.getAnswerByUserId("test-user")).thenReturn(Optional.of(answer));

        mockMvc.perform(get("/personalityAnswers/test-user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("test-user"));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        when(service.getAnswerByUserId("non-existent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/personalityAnswers/non-existent"))
                .andExpect(status().isNotFound());
    }
}