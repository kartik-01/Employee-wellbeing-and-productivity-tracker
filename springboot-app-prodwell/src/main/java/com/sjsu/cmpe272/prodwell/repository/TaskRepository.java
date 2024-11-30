package com.sjsu.cmpe272.prodwell.repository;

import com.sjsu.cmpe272.prodwell.entity.Task;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByUserId(String userId);
    Task findByTaskId(UUID taskId);
}
