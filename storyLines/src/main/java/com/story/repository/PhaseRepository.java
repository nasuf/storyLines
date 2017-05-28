package com.story.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.story.model.Phase;

public interface PhaseRepository extends MongoRepository<Phase, String>{

}
