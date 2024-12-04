package com.sjsu.cmpe272.prodwell.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sjsu.cmpe272.prodwell.entity.User;
import com.sjsu.cmpe272.prodwell.entity.UserDataDTO;
import com.sjsu.cmpe272.prodwell.service.UserService;
import com.sjsu.cmpe272.prodwell.service.UserDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDataService userDataService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCheckAndCreateUser() throws Exception {  // Add throws Exception
        User user = new User();
        user.setOid("test-oid");
        user.setGiven_name("John");
        user.setJobRole("Engineer");    // Use setJobRole instead of setJobTitle
        user.setJobLevel("Senior");     // Add job level
        user.setProjectCode("P1");      // Add project code

        when(userService.checkAndCreateUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/check-and-create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.oid").value("test-oid"));
    }

    @Test
    void shouldGetAIInsights() throws Exception {
        User user = new User();
        user.setOid("test-oid");
        UserDataDTO userData = new UserDataDTO(user);
        String aiAnalysis = "{\"analysis\": \"test\"}";

        when(userDataService.getUserData("test-oid")).thenReturn(userData);
        when(userService.analyzeUserStress(any(UserDataDTO.class))).thenReturn(aiAnalysis);

        mockMvc.perform(post("/api/users/test-oid/ai-insights"))
                .andExpect(status().isOk())
                .andExpect(content().string(aiAnalysis));
    }

    @Test
    void shouldReturn404WhenUserNotFoundForAIInsights() throws Exception {
        UserDataDTO emptyData = new UserDataDTO();
        when(userDataService.getUserData("non-existent")).thenReturn(emptyData);

        mockMvc.perform(post("/api/users/non-existent/ai-insights"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn500WhenAIAnalysisFails() throws Exception {
        User user = new User();
        user.setOid("test-oid");
        UserDataDTO userData = new UserDataDTO(user);

        when(userDataService.getUserData("test-oid")).thenReturn(userData);
        when(userService.analyzeUserStress(any(UserDataDTO.class))).thenReturn(null);

        mockMvc.perform(post("/api/users/test-oid/ai-insights"))
                .andExpect(status().isInternalServerError());
    }
}