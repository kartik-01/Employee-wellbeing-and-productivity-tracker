package com.sjsu.cmpe272.prodwell.repository;

import com.sjsu.cmpe272.prodwell.entity.AIInsights;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AIInsightsRepository extends MongoRepository<AIInsights, String> {
    Optional<AIInsights> findByOid(String oid);
}
