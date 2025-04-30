package com.goorm.thelastsupper.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "잘못된 이메일 유형입니다.")
	String email,

	@NotBlank(message = "비밀번호는 필수입니다.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d_!@#$%^&*\\-+=?]{8,16}$",
		message = "비밀번호는 8~16자 영문 대소문자, 숫자, 특수문자를 사용하세요.")
	String password
) {
}
