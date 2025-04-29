package com.goorm.thelastsupper.account.dto;

public record LoginResponse(
	TokenDTO tokenDTO,
	AccountResponse accountResponse

) {
	public LoginResponse onlyAccessToken(){
		return new LoginResponse(this.tokenDTO.onlyAccessToken(), this.accountResponse);
	}
}
