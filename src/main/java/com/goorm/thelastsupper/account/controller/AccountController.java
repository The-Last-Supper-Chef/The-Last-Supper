package com.goorm.thelastsupper.account.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.thelastsupper.account.dto.AccountResponse;
import com.goorm.thelastsupper.account.service.AccountService;
import com.goorm.thelastsupper.common.security.CustomPrincipal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class AccountController {

	private final AccountService accountService;

	@GetMapping("/customers")
	public ResponseEntity<AccountResponse> getAccount(@AuthenticationPrincipal CustomPrincipal customPrincipal) {
		AccountResponse response = accountService.getAccount(customPrincipal.getId());
		return ResponseEntity.ok(response);
	}

}
