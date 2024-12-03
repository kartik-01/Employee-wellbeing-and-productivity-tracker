package com.sjsu.cmpe272.prodwell.service;

import com.sjsu.cmpe272.prodwell.entity.*;
import com.sjsu.cmpe272.prodwell.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDataServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PersonalityAnswerRepository personalityAnswerRepository;

    @Mock
    private PersonalityQuestionRepository personalityQuestionRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private UserDataService service;

    @Test
    void shouldGetUserData() {
        String oid = "test-oid";
        User user = new User();
        user.setOid(oid);

        when(userRepository.findByOid(oid)).thenReturn(Optional.of(user));
        when(personalityAnswerRepository.findById(oid)).thenReturn(Optional.empty());
        when(taskRepository.findByUserId(oid)).thenReturn(Arrays.asList());

        UserDataDTO result = service.getUserData(oid);
        assertNotNull(result);
        assertEquals(user, result.getUser());
    }

    @Test
    void shouldCheckUserExists() {
        when(userRepository.existsByOid("test-oid")).thenReturn(true);
        assertTrue(service.userExists("test-oid"));
    }

    @Test
    void shouldGetUserDetails() {
        User user = new User();
        when(userRepository.findByOid("test-oid")).thenReturn(Optional.of(user));

        Optional<User> result = service.getUserDetails("test-oid");
        assertTrue(result.isPresent());
    }

    @Test
    void shouldGetUserTasks() {
        Task task = new Task();
        when(taskRepository.findByUserId("test-oid")).thenReturn(Arrays.asList(task));

        List<Task> results = service.getUserTasks("test-oid");
        assertFalse(results.isEmpty());
    }

    @Test
    void shouldReturnEmptyPersonalityDataWhenNoAnswersExist() {
        when(personalityAnswerRepository.findById(anyString())).thenReturn(Optional.empty());

        List<UserDataDTO.PersonalityData> results = service.getUserPersonalityAnswers("test-oid");
        assertTrue(results.isEmpty());
    }
}