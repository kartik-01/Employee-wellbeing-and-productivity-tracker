package com.sjsu.cmpe272.prodwell.controller;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.sjsu.cmpe272.prodwell.entity.ProductivityQuestion;
import com.sjsu.cmpe272.prodwell.service.ProductivityQuestionService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/productivityQuestions")
public class ProductivityQuestionController {

    @Autowired
    private ProductivityQuestionService service;

    @GetMapping
    public List<ProductivityQuestion> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ProductivityQuestion getById(@PathVariable ObjectId id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductivityQuestion add(@RequestBody ProductivityQuestion question) {
        return service.add(question);
    }

    @PutMapping("/{id}")
    public ProductivityQuestion update(@PathVariable ObjectId id, @RequestBody ProductivityQuestion question) {
        question.setId(id);
        return service.update(question);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable ObjectId id) {
        service.delete(id);
    }
}
