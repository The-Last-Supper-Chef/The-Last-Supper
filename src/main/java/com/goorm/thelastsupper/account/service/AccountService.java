package com.goorm.thelastsupper.account.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.thelastsupper.account.dto.AccountResponse;
import com.goorm.thelastsupper.account.dto.AccountUpdateRequest;
import com.goorm.thelastsupper.account.dto.PasswordRequest;
import com.goorm.thelastsupper.account.entity.Account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountValidationService accountValidationService;
	private final PasswordEncoder passwordEncoder;
	private final AuthService authService;

	public AccountResponse getAccount(String accountId) {
		Account account = accountValidationService.findById(accountId);
		return AccountResponse.toAccountResponse(account);
	}

	@Transactional
	public AccountResponse updateAccount(String accountId, AccountUpdateRequest accountUpdateRequest) {
		Account account = accountValidationService.findById(accountId);
		account.update(accountUpdateRequest.nickName(), accountUpdateRequest.phone());
		return AccountResponse.toAccountResponse(account);
	}

	@Transactional
	public AccountResponse updatePassword(String accountId, PasswordRequest passwordRequest) {
		Account account = accountValidationService.findById(accountId);
		accountValidationService.checkPassword(passwordRequest.curPassword(), account.getPassword());
		account.updatePassword(passwordEncoder.encode(passwordRequest.newPassword()));
		authService.deleteAllToken(account.getId());
		return AccountResponse.toAccountResponse(account);
	}
}
