package com.goorm.thelastsupper.account.controller;

import java.net.URI;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.thelastsupper.account.dto.LoginRequest;
import com.goorm.thelastsupper.account.dto.LoginResponse;
import com.goorm.thelastsupper.account.dto.SignupRequest;
import com.goorm.thelastsupper.account.dto.TokenDTO;
import com.goorm.thelastsupper.account.service.AuthService;
import com.goorm.thelastsupper.common.security.exception.AuthException;
import com.goorm.thelastsupper.common.util.HeaderUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest signupRequest) {
		authService.signup(signupRequest);
		return ResponseEntity.created(URI.create("/api/v1/customer")).build();
	}

	@PostMapping("/login")
	public ResponseEntity<?> studentLogin(@RequestBody @Valid LoginRequest loginRequest){
		LoginResponse loginResponse = authService.login(loginRequest);

		ResponseCookie cookie = ResponseCookie.from("refreshToken",loginResponse.tokenDTO().refreshToken())
			.maxAge(14*24*60*60)
			.path("/")
			.secure(true)
			.sameSite("None")
			.httpOnly(true)
			.build();
		return ResponseEntity.ok().header("Set-Cookie",cookie.toString()).body(loginResponse.onlyAccessToken());
	}

	@PostMapping("/refresh")
	public ResponseEntity<TokenDTO> refreshToken(@RequestHeader(HeaderUtil.HEADER_AUTHORIZATION) String access, @CookieValue(value = "refreshToken",required = false) String cookieRefresh) {
		if(cookieRefresh==null || access ==null){
			throw new AuthException.RefreshTokenMissingException();
		}
		TokenDTO tokenDTO = authService.refresh(HeaderUtil.getAccessToken(access), cookieRefresh);
		ResponseCookie cookie = ResponseCookie.from("refreshToken",tokenDTO.refreshToken())
			.maxAge(14*24*60*60)
			.path("/")
			.secure(true)
			.sameSite("None")
			.httpOnly(true)
			.build();
		return ResponseEntity.ok().header("Set-Cookie",cookie.toString()).body(tokenDTO.onlyAccessToken());
	}

}
