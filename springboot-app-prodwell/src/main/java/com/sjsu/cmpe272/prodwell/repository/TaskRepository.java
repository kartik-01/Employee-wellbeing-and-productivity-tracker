package com.sjsu.cmpe272.prodwell.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.sjsu.cmpe272.prodwell.entity.Task;

public interface TaskRepository extends MongoRepository<Task,String>{

	List<Task> findBySeverity(int severity);
	
	@Query("{assignee: ?0 }")
	List<Task> getTasksByAssignee(String assignee);
}
