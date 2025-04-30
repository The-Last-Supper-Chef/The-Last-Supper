package com.goorm.thelastsupper.account.service;

import org.springframework.stereotype.Service;

import com.goorm.thelastsupper.account.dto.AccountResponse;
import com.goorm.thelastsupper.account.entity.Account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountValidationService accountValidationService;

	public AccountResponse getAccount(String accountId) {
		Account account = accountValidationService.findById(accountId);
		return AccountResponse.toAccountResponse(account);
	}
}
