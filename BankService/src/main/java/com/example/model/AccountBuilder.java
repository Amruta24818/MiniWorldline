package com.example.model;

public class AccountBuilder {
	private String accountNo;
	private String accountHolderName;
	private String ifsc;
	private float balance;
	private String branch;
	public CardDetail cardDetails;
	
	public AccountBuilder setAccountNo(String accountNo) {
		this.accountNo = accountNo;
		return this;
	}
	public AccountBuilder setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
		return this;
	}
	public AccountBuilder setIfsc(String ifsc) {
		this.ifsc = ifsc;
		return this;
	}
	public AccountBuilder setBalance(float balance) {
		this.balance = balance;
		return this;
	}
	public AccountBuilder setBranch(String branch) {
		this.branch = branch;
		return this;
	}
	public AccountBuilder setCardDetails(CardDetail cardDetails) {
		this.cardDetails = cardDetails;
		return this;
	}
	
	public Account getAccount()
	{
		return new Account(accountNo,accountHolderName,ifsc,balance,branch,cardDetails);
	}
}
