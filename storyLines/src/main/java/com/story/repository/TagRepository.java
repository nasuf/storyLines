package com.story.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.story.model.Tag;

public interface TagRepository extends MongoRepository<Tag, String> {
	
	Tag findByValue(String value);

}
