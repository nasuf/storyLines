package com.story.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.story.model.Phase;

public interface PhaseRepository extends MongoRepository<Phase, String>{
	
	@Query("{$and:[{'storyId':{'$eq': ?0}},{'level':{'$eq': ?1}}]}")
	List<Phase> findByStoryIdAndLevel(String storyId, Integer level);
	
	@Query("{$and:[{'storyId':{'$eq': ?0}},{'parentPhaseId':{'$eq': ?1}}]}")
	List<Phase> findByStoryIdAndParentPhaseId(String storyId, String parentPhaseId);
	
	@Query("{'level':{'$eq':?0}}")
	List<Phase> findByLevel(Integer level);

}
