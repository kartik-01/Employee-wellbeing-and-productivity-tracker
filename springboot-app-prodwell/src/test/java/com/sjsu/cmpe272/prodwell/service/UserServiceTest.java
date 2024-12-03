package com.sjsu.cmpe272.prodwell.service;

import com.sjsu.cmpe272.prodwell.entity.User;
import com.sjsu.cmpe272.prodwell.entity.UserDataDTO;
import com.sjsu.cmpe272.prodwell.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

    @Test
    void shouldCreateNewUser() {
        User userData = new User();
        userData.setOid("test-oid");
        userData.setGiven_name("John");

        when(userRepository.findByOid("test-oid")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userData);

        User result = service.checkAndCreateUser(userData);
        assertNotNull(result);
        assertEquals("John", result.getGiven_name());
    }

    @Test
    void shouldReturnExistingUser() {
        User existingUser = new User();
        existingUser.setOid("test-oid");
        existingUser.setGiven_name("John");

        when(userRepository.findByOid("test-oid")).thenReturn(Optional.of(existingUser));

        User result = service.checkAndCreateUser(existingUser);
        assertNotNull(result);
        assertEquals("John", result.getGiven_name());
        verify(userRepository, never()).save(any(User.class));
    }
}