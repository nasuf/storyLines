package com.story.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	@Query("{'parentPhaseId':{'$eq':?0}}")	
	Page<Phase> findByParentPhaseId(String parentPhaseId, Pageable page);
	
	@Query("{$and:[{'parentPhaseId':{'$eq':?0}}, {'tags':{'$in':?1}}]}")	
	Page<Phase> findByParentPhaseIdAndTags(String parentPhaseId, Object tags, Pageable page);
	
	@Query("{$and:[{'parentPhaseId':{'$eq':?0}}, {'authorOpenid':{'$eq':?1}}]}")
	Page<Phase> findByParentPhaseIdAndOpenid(String parentPhaseId, String openid, Pageable page);
	
	@Query("{$and:[{'parentPhaseId':{'$ne':?0}}, {'authorOpenid':{'$eq':?1}}]}")
	Page<Phase> findByParentPhaseIdNotNullAndOpenid(String parentPhaseId, String openid, Pageable page);
	
	@Query("{'parentPhaseId':{'$eq':?0}}")
	Phase findByParentPhaseId(String parentPhaseId);

	Phase findById(String phaseId);
	
	@Query("{'_id':{'$in':?0}}")
	Page<Phase> findByIdIn(List<String> branchPhases, Pageable page);

}
