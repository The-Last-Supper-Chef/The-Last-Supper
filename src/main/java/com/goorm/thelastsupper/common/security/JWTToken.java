package com.goorm.thelastsupper.common.security;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RedisHash(value = "mimoToken", timeToLive = 14*24*60*60) //1,209,600
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JWTToken{
	@Id
	private String refreshToken;
	private String accessToken;
}
