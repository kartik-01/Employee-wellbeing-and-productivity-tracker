package com.sjsu.cmpe272.prodwell.repository;

import com.sjsu.cmpe272.prodwell.entity.ProductivityAnswer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductivityAnswerRepository extends MongoRepository<ProductivityAnswer, ObjectId> {
    List<ProductivityAnswer> findByUserId(ObjectId userId);
}
