package com.sjsu.cmpe272.prodwell.service;

import com.sjsu.cmpe272.prodwell.entity.ProductivityAnswer;
import com.sjsu.cmpe272.prodwell.repository.ProductivityAnswerRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductivityAnswerService {

    @Autowired
    private ProductivityAnswerRepository repository;

    public ProductivityAnswer saveAnswer(ProductivityAnswer answer) {
        answer.setDateTime(LocalDateTime.now());
        return repository.save(answer);
    }

    public List<ProductivityAnswer> getAnswersByUserId(ObjectId userId) {
        return repository.findByUserId(userId);
    }
}
