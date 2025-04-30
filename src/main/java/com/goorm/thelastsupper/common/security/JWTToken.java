package com.goorm.thelastsupper.common.security;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RedisHash(value = "jwtToken", timeToLive = 14*24*60*60)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JWTToken{
	@Id
	private String refreshToken;
	private String accessToken;
}
