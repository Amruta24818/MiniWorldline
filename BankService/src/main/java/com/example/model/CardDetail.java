package com.example.model;

import java.util.Date;

public class CardDetail {
	private String cardNo;
	private String cardHolderName;
	private String cvv;
	private Date expireDate;

	public CardDetail() {
		// TODO Auto-generated constructor stub
	}

	public CardDetail(String cardNo, String cardHolderName, String cvv, Date expireDate) {
		super();
		this.cardNo = cardNo;
		this.cardHolderName = cardHolderName;
		this.cvv = cvv;
		this.expireDate = expireDate;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	@Override
	public String toString() {
		return "CardDetail [cardNo=" + cardNo + ", cardHolderName=" + cardHolderName + ", cvv=" + cvv + ", expireDate="
				+ expireDate + "]";
	}

}
