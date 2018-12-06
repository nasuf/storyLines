package com.story.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.story.model.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String> {
	
	@Query("{$and:[{'destination':{'$eq': ?0}},{'isStoryUpdateNotification':{'$eq': ?1}}]}")
	Notification findByDestinationAndIsStoryUpdateNotification(String destination, Boolean isStoryUpdateNotification);
	
	@Query("{'destination':{'$eq':?0}}")
	List<Notification> findByDestination(String destination);
	
	@Query("{'isStoryUpdateNotification':{'$eq':?0}}")
	Notification findByIsStoryUpdateNotification(Boolean isStoryUpdateNotification);

}
