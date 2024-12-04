package com.sjsu.cmpe272.prodwell.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjsu.cmpe272.prodwell.entity.AIInsights;
import com.sjsu.cmpe272.prodwell.entity.User;
import com.sjsu.cmpe272.prodwell.entity.UserDataDTO;
import com.sjsu.cmpe272.prodwell.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDataService userDataService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AIInsightsService aiInsightsService;

    @InjectMocks
    private UserService service;

    @Mock
    private ObjectMapper objectMapper;

    private static final String PPLX_API_URL = "https://api.perplexity.ai/chat/completions";

    @Test
    void shouldCreateNewUser() {
        User userData = new User();
        userData.setOid("test-oid");
        userData.setGiven_name("John");
        userData.setJobRole("Engineer");
        userData.setJobLevel("Senior");
        userData.setProjectCode("P1");

        when(userRepository.findByOid("test-oid")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userData);

        User result = service.checkAndCreateUser(userData);
        assertNotNull(result);
        assertEquals("John", result.getGiven_name());
        assertEquals("Engineer", result.getJobRole());
        assertEquals("Senior", result.getJobLevel());
    }

    @Test
    void shouldReturnExistingUser() {
        User existingUser = new User();
        existingUser.setOid("test-oid");
        existingUser.setGiven_name("John");
        existingUser.setJobRole("Engineer");
        existingUser.setJobLevel("Senior");

        when(userRepository.findByOid("test-oid")).thenReturn(Optional.of(existingUser));

        User result = service.checkAndCreateUser(existingUser);
        assertNotNull(result);
        assertEquals("John", result.getGiven_name());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldGetUserCompleteData() {
        String oid = "test-oid";
        UserDataDTO userData = new UserDataDTO();
        User user = new User();
        user.setOid(oid);
        userData.setUser(user);

        when(userDataService.getUserData(oid)).thenReturn(userData);

        UserDataDTO result = service.getUserCompleteData(oid);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForInvalidUserData() {
        String oid = "invalid-oid";
        UserDataDTO emptyData = new UserDataDTO();

        when(userDataService.getUserData(oid)).thenReturn(emptyData);

        UserDataDTO result = service.getUserCompleteData(oid);
        assertNull(result);
    }

    @Test
    void shouldAnalyzeUserStress() throws Exception {
        UserDataDTO userData = new UserDataDTO();
        User user = new User();
        user.setOid("test-oid");
        userData.setUser(user);

        String mockAIResponse = "{\"choices\":[{\"message\":{\"content\":" +
                "{\"dailyStressLevels\":[],\"averageStressLevel\":5.0,\"analysis\":{" +
                "\"overview\":\"test overview\"," +
                "\"workloadAnalysis\":\"test analysis\"," +
                "\"suggestions\":{" +
                "\"taskManagement\":[\"test suggestion\"]," +
                "\"personalWellbeing\":[\"test wellbeing\"]" +
                "}}}}}]}";

        JsonNode mockRootNode = new ObjectMapper().readTree(mockAIResponse);
        when(objectMapper.writeValueAsString(any(UserDataDTO.class))).thenReturn("{}");
        when(objectMapper.readTree(anyString())).thenReturn(mockRootNode);

        ResponseEntity<String> mockResponse = ResponseEntity.ok(mockAIResponse);
        when(restTemplate.exchange(
                eq(PPLX_API_URL),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponse);

        when(aiInsightsService.saveInsight(anyString(), anyString())).thenReturn(new AIInsights());

        String result = service.analyzeUserStress(userData);
        assertNotNull(result);
        verify(aiInsightsService).saveInsight(eq("test-oid"), anyString());
    }

    @Test
    void shouldHandleExceptionInAnalyzeUserStress() throws Exception {
        UserDataDTO userData = new UserDataDTO();
        User user = new User();
        user.setOid("test-oid");
        userData.setUser(user);

        when(objectMapper.writeValueAsString(any(UserDataDTO.class)))
                .thenThrow(new RuntimeException("Test exception"));

        String result = service.analyzeUserStress(userData);
        assertNull(result);
    }
}