package com.goorm.thelastsupper.account.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.goorm.thelastsupper.account.dto.AccountResponse;
import com.goorm.thelastsupper.account.dto.LoginRequest;
import com.goorm.thelastsupper.account.dto.LoginResponse;
import com.goorm.thelastsupper.account.dto.SignupRequest;
import com.goorm.thelastsupper.account.dto.TokenDTO;
import com.goorm.thelastsupper.account.entity.Account;
import com.goorm.thelastsupper.common.security.JWTToken;
import com.goorm.thelastsupper.common.security.JWTTokenRepository;
import com.goorm.thelastsupper.common.security.TokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final AccountValidationService accountValidationService;
	private final PasswordEncoder passwordEncoder;
	private final JWTTokenRepository jwtTokenRepository;
	private final TokenProvider tokenProvider;

	public void signup(SignupRequest signupRequest) {
		accountValidationService.existAccount(signupRequest.email());
		accountValidationService.save(signupRequest.toAccount(passwordEncoder.encode(signupRequest.password())));
	}

	public LoginResponse login(LoginRequest loginRequest) {
		Account account = accountValidationService.validExistUser(loginRequest.email());
		accountValidationService.checkPassword(loginRequest.password(), account.getPassword());
		return new LoginResponse(saveToken(account), AccountResponse.toAccountResponse(account));
	}

	private TokenDTO saveToken(Account account){
		JWTToken refreshToken = makeToken(account);
		jwtTokenRepository.save(refreshToken);
		return new TokenDTO(refreshToken.getAccessToken(), refreshToken.getRefreshToken());
	}

	private JWTToken makeToken(Account account){
		return new JWTToken(tokenProvider.createRefreshToken(account.getId()), tokenProvider.createAccessToken(account));
	}

}
