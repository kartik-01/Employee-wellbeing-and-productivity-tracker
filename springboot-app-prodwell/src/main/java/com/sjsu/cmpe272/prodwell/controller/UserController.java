package com.sjsu.cmpe272.prodwell.controller;

import com.sjsu.cmpe272.prodwell.entity.User;
import com.sjsu.cmpe272.prodwell.entity.UserDataDTO;
import com.sjsu.cmpe272.prodwell.service.UserService;
import com.sjsu.cmpe272.prodwell.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserDataService userDataService;

    @PostMapping("/check-and-create")
    public ResponseEntity<User> checkAndCreateUser(@RequestBody User userData) {
        System.out.println("Received user data: " + userData);
        User user = userService.checkAndCreateUser(userData);
        System.out.println("Saved user: " + user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{oid}/complete-data")
    public ResponseEntity<UserDataDTO> getUserCompleteData(@PathVariable String oid) {
        UserDataDTO userData = userDataService.getUserData(oid);
        if (userData.getUser() != null) {
            return ResponseEntity.ok(userData);
        }
        return ResponseEntity.notFound().build();
    }
}