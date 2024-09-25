package com.sjsu.cmpe272.prodwell.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sjsu.cmpe272.prodwell.entity.User;

public interface UserRepository extends MongoRepository<User,String>{

	 Optional<User> findByOid(String oid);
}
