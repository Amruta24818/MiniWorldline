package com.example.dto;

import com.example.model.Account;

public class ResponseDTO {
	private String otp;
	private Account account;

	public ResponseDTO(String otp, Account account) {
		super();
		this.otp = otp;
		this.account = account;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return "ResponseDTO [otp=" + otp + ", account=" + account + "]";
	}

}
