package com.sjsu.cmpe272.prodwell.repository;

import com.sjsu.cmpe272.prodwell.entity.AIInsights;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AIInsightsRepository extends MongoRepository<AIInsights, String> {
    Optional<AIInsights> findByOid(String oid);
    List<AIInsights> findByOidIn(Set<String> oids);
}
