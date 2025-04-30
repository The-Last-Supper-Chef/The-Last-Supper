package com.goorm.thelastsupper.account.dto;

import com.goorm.thelastsupper.account.entity.Account;

import lombok.Builder;

@Builder
public record AccountResponse(
	String email,
	String nickName,
	String phone
) {
	public static AccountResponse toAccountResponse(Account account) {
		return AccountResponse.builder()
			.email(account.getEmail())
			.nickName(account.getNickName())
			.phone(account.getPhoneNumber())
			.build();
	}
}
