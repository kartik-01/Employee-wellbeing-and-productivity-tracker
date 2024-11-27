package com.sjsu.cmpe272.prodwell.service;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sjsu.cmpe272.prodwell.entity.ProductivityQuestion;
import com.sjsu.cmpe272.prodwell.repository.ProductivityQuestionRepository;

@Service
public class ProductivityQuestionService {

    @Autowired
    private ProductivityQuestionRepository repository;

    public List<ProductivityQuestion> getAll() {
        return repository.findAll();
    }

    public ProductivityQuestion getById(ObjectId id) {
        return repository.findById(id).orElse(null);
    }

    public ProductivityQuestion add(ProductivityQuestion question) {
        return repository.save(question);
    }

    public ProductivityQuestion update(ProductivityQuestion question) {
        return repository.save(question);
    }

    public void delete(ObjectId id) {
        repository.deleteById(id);
    }
}
