package com.goorm.thelastsupper.common.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.goorm.thelastsupper.common.security.exception.AuthException;
import com.goorm.thelastsupper.common.util.HeaderUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthFilter extends OncePerRequestFilter {
	private final TokenProvider tokenProvider;

	private final List<String> notJwtPaths = List.of(
		"/api/v1/signup",
		"/api/v1/login",
		"/api/refresh"
	);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String uri = request.getRequestURI();

		if (!notJwtPaths.contains(uri)) {
			try {
				String token = HeaderUtil.getAccessToken(request);
				Authentication auth = tokenProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(auth);
				log.info("인증 성공: {}", auth.getName());
			} catch (ExpiredJwtException e) {
				throw new AuthException.TokenExpiredException();
			} catch (UnsupportedJwtException e) {
				throw new AuthException.UnsupportedTokenException();
			} catch (SignatureException | SecurityException | MalformedJwtException e) {
				throw new AuthException.TokenParsingException();
			} catch (IllegalArgumentException e) {
				throw new AuthException.InvalidAuthHeaderException();
			}
		}

		filterChain.doFilter(request, response);
	}
}
