package com.goorm.thelastsupper.account.service;

import java.util.List;

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
import com.goorm.thelastsupper.common.security.exception.AuthException;

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

	public TokenDTO refresh(String access, String refresh){
		validRefreshToken(access,refresh);
		return saveToken(accountValidationService.findById(tokenProvider.getAccountId(refresh)));
	}

	public void deleteAllToken(String accountId) {
		List<JWTToken> tokens = jwtTokenRepository.findByAccountId(accountId);
		log.info("삭제할 토큰 개수: {}", tokens.size());
		jwtTokenRepository.deleteAll(tokens);
	}

	private TokenDTO saveToken(Account account){
		JWTToken refreshToken = makeToken(account);
		jwtTokenRepository.save(refreshToken);
		return new TokenDTO(refreshToken.getAccessToken(), refreshToken.getRefreshToken());
	}

	private JWTToken makeToken(Account account){
		return new JWTToken(tokenProvider.createRefreshToken(account.getId()), tokenProvider.createAccessToken(account), account.getId());
	}

	private void validRefreshToken(String access, String refresh){
		JWTToken token = jwtTokenRepository.findById(refresh).orElseThrow(AuthException.RefreshTokenNotFoundException::new);
		if(!token.getAccessToken().equals(access)) {
			throw new AuthException.RefreshTokenInvalidException();
		}
		jwtTokenRepository.delete(token);
	}

}
