package com.goorm.thelastsupper.account.dto;

public record TokenDTO(
	String accessToken,
	String refreshToken
) {
	public TokenDTO onlyAccessToken(){
		return new TokenDTO(this.accessToken,"cookie");
	}

}
