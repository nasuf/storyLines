package com.story.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.story.model.Story;

public interface StoryRepository extends MongoRepository<Story, String>{

}
