package com.goorm.thelastsupper.common.security;

import java.util.Optional;

import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

@EnableRedisRepositories
public interface JWTTokenRepository extends CrudRepository<JWTToken, String> {
	Optional<JWTToken> findByRefreshToken(String refresh);
	Optional<JWTToken> findByAccessToken(String refresh);
	Optional<JWTToken> findByRefreshTokenAndAccessToken(String refresh, String access);
}
