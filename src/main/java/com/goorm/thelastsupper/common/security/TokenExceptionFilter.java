package com.goorm.thelastsupper.common.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goorm.thelastsupper.common.dto.ErrorResponse;
import com.goorm.thelastsupper.common.security.exception.AuthErrorCode;
import com.goorm.thelastsupper.common.security.exception.AuthException;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenExceptionFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws
		ServletException,
		IOException {
		try {
			chain.doFilter(req, res);
		} catch (JwtException | UsernameNotFoundException | AuthException | AccessDeniedException ex) {
			setErrorResponse(HttpStatus.UNAUTHORIZED, res, ex);
		}
	}

	public void setErrorResponse(HttpStatus status, HttpServletResponse res, Throwable ex) throws IOException {
		res.setStatus(status.value());
		res.setContentType("application/json; charset=UTF-8");

		ErrorResponse dto = new ErrorResponse(AuthErrorCode.TOKEN_PARSING_FAILED.name(), ex.getMessage());

		String json = new ObjectMapper().writeValueAsString(dto);
		res.setStatus(status.value());
		res.getWriter().write(json);
	}

}
