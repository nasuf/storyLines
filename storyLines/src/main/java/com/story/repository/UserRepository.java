package com.story.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.story.model.User;

public interface UserRepository extends MongoRepository<User, String>{

	User findByOpenid(String openid);
	
	Page<User> findByRole(String role, Pageable page);
	
	List<User> findByRole(String role);
	
	List<User> findByNickNameLikeIgnoreCase(String nickName);
	
	List<User> findByWechatIdLikeIgnoreCase(String wechatId);

}
