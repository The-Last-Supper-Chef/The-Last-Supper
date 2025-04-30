package com.goorm.thelastsupper.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AccountUpdateRequest(
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
	String nickName
) {
}
