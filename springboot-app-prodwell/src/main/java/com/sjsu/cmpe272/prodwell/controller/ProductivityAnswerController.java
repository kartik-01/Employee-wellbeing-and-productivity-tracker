package com.sjsu.cmpe272.prodwell.controller;

import com.sjsu.cmpe272.prodwell.entity.ProductivityAnswer;
import com.sjsu.cmpe272.prodwell.service.ProductivityAnswerService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productivityAnswers")
public class ProductivityAnswerController {

    @Autowired
    private ProductivityAnswerService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductivityAnswer recordAnswer(@RequestBody ProductivityAnswer answer) {
        return service.saveAnswer(answer);
    }

    @GetMapping("/user/{userId}")
    public List<ProductivityAnswer> getAnswersByUserId(@PathVariable ObjectId userId) {
        return service.getAnswersByUserId(userId);
    }
}
