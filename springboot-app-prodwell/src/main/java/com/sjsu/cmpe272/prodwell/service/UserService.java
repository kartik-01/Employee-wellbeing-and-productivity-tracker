package com.sjsu.cmpe272.prodwell.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sjsu.cmpe272.prodwell.entity.User;
import com.sjsu.cmpe272.prodwell.entity.UserDataDTO;
import com.sjsu.cmpe272.prodwell.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDataService userDataService;

    public User checkAndCreateUser(User userData) {
        return userRepository.findByOid(userData.getOid())
            .orElseGet(() -> createNewUser(userData));
    }

    private User createNewUser(User userData) {
        User newUser = new User();
        newUser.setOid(userData.getOid());
        newUser.setGiven_name(userData.getGiven_name());
        newUser.setFamily_name(userData.getFamily_name());
        newUser.setJobTitle(userData.getJobTitle());
        return userRepository.save(newUser);
    }

    public UserDataDTO getUserCompleteData(String oid) {
        UserDataDTO userData = userDataService.getUserData(oid);
        if (userData.getUser() != null) {
            return userData;
        }
        return null;
    }
    
}