package com.example.model;

public class PaymentRecord {

	private String cardNo;
	private String transactionId;
	private String paymentInitiate;
	private String authorization;
	private String paymentResult;

	public PaymentRecord() {
		super();
	}

	public PaymentRecord(String cardNo, String transactionId, String paymentInitiate, String authorization,
			String paymentResult) {
		super();
		this.cardNo = cardNo;
		this.transactionId = transactionId;
		this.paymentInitiate = paymentInitiate;
		this.authorization = authorization;
		this.paymentResult = paymentResult;

	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getPaymentInitiate() {
		return paymentInitiate;
	}

	public void setPaymentInitiate(String paymentInitiate) {
		this.paymentInitiate = paymentInitiate;
	}

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	public String getPaymentResult() {
		return paymentResult;
	}

	public void setPaymentResult(String paymentResult) {
		this.paymentResult = paymentResult;
	}

	@Override
	public String toString() {
		return "PaymentRecord [cardNo=" + cardNo + ", transactionId=" + transactionId + ", paymentInitiate="
				+ paymentInitiate + ", authorization=" + authorization + ", paymentResult=" + paymentResult + "]";
	}

}
