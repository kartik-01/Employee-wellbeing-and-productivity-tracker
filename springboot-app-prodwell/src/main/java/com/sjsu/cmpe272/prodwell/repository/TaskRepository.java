package com.sjsu.cmpe272.prodwell.repository;

import com.sjsu.cmpe272.prodwell.entity.Task;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByOid(String oid);
    Task findByTaskId(String taskId);
}
