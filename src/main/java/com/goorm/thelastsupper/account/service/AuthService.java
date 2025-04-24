package com.goorm.thelastsupper.account.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.goorm.thelastsupper.account.dto.SignupRequest;
import com.goorm.thelastsupper.account.exception.AccountException;
import com.goorm.thelastsupper.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;

	public void signup(SignupRequest signupRequest) {
		if (accountRepository.existsByEmail(signupRequest.email())) {
			throw new AccountException.AccountDuplicationException();
		}
		accountRepository.save(signupRequest.toAccount(passwordEncoder.encode(signupRequest.password())));
	}

}
