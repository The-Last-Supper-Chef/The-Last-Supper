package com.goorm.thelastsupper.account.dto;

import com.goorm.thelastsupper.account.entity.Account;
import com.goorm.thelastsupper.account.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequest(
	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "잘못된 이메일 유형입니다.")
	String email,

	@NotBlank(message = "전화번호는 필수입니다.")
	@Pattern(
		regexp = "^\\d{3}-\\d{4}-\\d{4}$",
		message = "전화번호는 000-0000-0000 형식이어야 합니다."
	)
	String phone,

	@NotBlank(message = "닉네임은 필수입니다.")
	@Size(
		min = 2, max = 8,
		message = "닉네임은 2자 이상 8자 이하여야 합니다."
	)
	String nickName,

	@NotBlank(message = "비밀번호는 필수입니다.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d_!@#$%^&*\\-+=?]{8,16}$",
		message = "비밀번호는 8~16자 영문 대소문자, 숫자, 특수문자를 사용하세요.")
	String password
) {

	public Account toAccount(String hashedPassword) {
		return Account.builder()
			.role(Role.CUSTOMER)
			.nickName(nickName)
			.deleted(false)
			.phoneNumber(phone)
			.email(email)
			.password(hashedPassword)
			.build();
	}
}
