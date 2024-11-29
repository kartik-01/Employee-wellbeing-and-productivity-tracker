package com.sjsu.cmpe272.prodwell.repository;

import com.sjsu.cmpe272.prodwell.entity.UserTaskData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserTaskRepository extends MongoRepository<UserTaskData, String> {
}