package com.example.model;

public class Account {

	private String accountNo;
	private String accountHolderName;
	private String ifsc;
	private float balance;
	private String branch;
	public CardDetail cardDetails;

	public Account(String accountNo, String accountHolderName, String ifsc, float balance, String branch,
			CardDetail cardDetails) {
		super();
		this.accountNo = accountNo;
		this.accountHolderName = accountHolderName;
		this.ifsc = ifsc;
		this.balance = balance;
		this.branch = branch;
		this.cardDetails = cardDetails;
	}

	public Account() {
		super();
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public CardDetail getCardDetails() {
		return cardDetails;
	}

	public void setCardDetails(CardDetail cardDetails) {
		this.cardDetails = cardDetails;
	}

	@Override
	public String toString() {
		return "Account [accountNo=" + accountNo + ", accountHolderName=" + accountHolderName + ", ifsc=" + ifsc
				+ ", balance=" + balance + ", branch=" + branch + ", cardDetails=" + cardDetails + "]";
	}

}
