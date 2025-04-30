package com.goorm.thelastsupper.common.security;

import com.goorm.thelastsupper.account.entity.Role;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomPrincipal {
	private static final String AUTHORITIES_KEY = "role";
	private String id;
	private Role role;

	public static CustomPrincipal of(Claims claims){
		return new CustomPrincipal(claims.getSubject(), Role.of(claims.get(AUTHORITIES_KEY, String.class)));
	}
}
