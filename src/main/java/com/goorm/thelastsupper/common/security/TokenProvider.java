package com.goorm.thelastsupper.common.security;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.goorm.thelastsupper.account.dto.TokenDTO;
import com.goorm.thelastsupper.account.entity.Account;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenProvider {
	private final Key key;
	private static final String AUTHORITIES_KEY = "role";

	@Value("${spring.jwt.access-token-expire}")
	private long ACCESS_EXPIRE_MINUTES;
	@Value("${spring.jwt.refresh-token-expire}")
	private long REFRESH_EXPIRE_DAYS;

	public TokenProvider(@Value("${spring.jwt.secret}")String secret) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	public String createAccessToken(Account account) {
		Date now = new Date();
		Date exp = new Date(now.getTime() + ACCESS_EXPIRE_MINUTES);
		return Jwts.builder()
			.setSubject(account.getId())
			.claim("role", account.getRole().getCode())
			.signWith(key, SignatureAlgorithm.HS256)
			.setIssuedAt(now)
			.setExpiration(exp)
			.compact();
	}

	public String createRefreshToken(String accountId) {
		final Date now = new Date();
		final Date expiryDate = new Date(now.getTime() + REFRESH_EXPIRE_DAYS);

		return Jwts.builder()
			.setSubject(accountId)
			.signWith(key, SignatureAlgorithm.HS256)
			.setExpiration(expiryDate)
			.compact();
	}

	public Claims getTokenClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public String getAccountId(String token) {
		return getTokenClaims(token).getSubject();
	}

	public Authentication getAuthentication(String token) {
		Claims claims = getTokenClaims(token);
		Collection<? extends GrantedAuthority> authorities =
			Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
				.map(SimpleGrantedAuthority::new)
				.toList();
		log.debug("claims subject := [{}]", claims.getSubject());
		CustomPrincipal userInfo = CustomPrincipal.of(claims);
		return new UsernamePasswordAuthenticationToken(userInfo, token, authorities);
	}
}
