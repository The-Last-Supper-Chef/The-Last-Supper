package com.goorm.thelastsupper.common.security;

import java.util.List;

import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

@EnableRedisRepositories
public interface JWTTokenRepository extends CrudRepository<JWTToken, String> {
	List<JWTToken> findByAccountId(String id);
}
