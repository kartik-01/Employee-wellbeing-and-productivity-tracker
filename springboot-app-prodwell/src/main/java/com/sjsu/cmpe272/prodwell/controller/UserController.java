package com.sjsu.cmpe272.prodwell.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sjsu.cmpe272.prodwell.entity.User;
import com.sjsu.cmpe272.prodwell.service.UserService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class UserController {

	
    @Autowired
    private UserService userService;

    @PostMapping("/check-and-create")
    public ResponseEntity<User> checkAndCreateUser(@RequestBody User userData) {
        System.out.println("Received user data: " + userData);
        User user = userService.checkAndCreateUser(userData);
        System.out.println("Saved user: " + user);
        return ResponseEntity.ok(user);
    }
	
}

