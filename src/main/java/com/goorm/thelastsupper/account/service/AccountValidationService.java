package com.goorm.thelastsupper.account.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.goorm.thelastsupper.account.dto.LoginResponse;
import com.goorm.thelastsupper.account.entity.Account;
import com.goorm.thelastsupper.account.exception.AccountException;
import com.goorm.thelastsupper.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountValidationService {

	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;

	public void existAccount(String email){
		if (accountRepository.existsByEmail(email)) {
			throw new AccountException.AccountDuplicationException();
		}
	}

	public void save(Account account){
		accountRepository.save(account);
	}

	public Account validExistUser(String email){
		Account account = accountRepository.findByEmail(email).orElseThrow(AccountException.AccountNotFoundException::new);
		if (account.isDeleted()) {
			throw new AccountException.AccountWithdrawnException();
		}
		return account;
	}

	public void checkPassword(String inputPassword, String AccountPassword){
		if(!passwordEncoder.matches(inputPassword, AccountPassword)) {
			log.info("잘못된 비밀번호 입니다");
			throw new AccountException.AccountNotFoundException();
		}
	}
}
