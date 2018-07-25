package com.fundoonotes.read.repository;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

	private RedisTemplate<String, Object> redisTemplate;
	private HashOperations<String, String, Object> hashOperations;

	@Autowired
	public UserRepository(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}

	public Object getUserById(String key, String hashKey) {
		return hashOperations.get(key, hashKey);
	}

}
