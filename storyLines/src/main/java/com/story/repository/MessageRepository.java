package com.story.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.story.model.Message;

public interface MessageRepository extends MongoRepository<Message, String> {
	
	@Query("{'storyAuthorOpenid':{'$eq':?0}}")
	List<Message> findByStoryAuthorOpenid(String openid);
	
	@Query("{'phaseAuthorOpenid':{'$eq':?0}}")
	List<Message> findByPhaseAuthorOpenid(String openid);

}
