package com.example.dto;

public class ResponseDTO {
	private String otp;
	private Object account;

	public ResponseDTO(String otp, Object account) {
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

	public Object getAccount() {
		return account;
	}

	public void setAccount(Object account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return "ResponseDTO [otp=" + otp + ", account=" + account + "]";
	}

}
