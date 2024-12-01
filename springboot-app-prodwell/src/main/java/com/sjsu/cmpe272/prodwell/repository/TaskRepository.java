package com.sjsu.cmpe272.prodwell.repository;

import com.sjsu.cmpe272.prodwell.entity.Task;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, ObjectId> {
    List<Task> findByUserId(String userId);
    Task findByTaskId(String taskId);
}