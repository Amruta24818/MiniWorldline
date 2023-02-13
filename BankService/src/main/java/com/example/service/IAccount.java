package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.dto.MerchantReqDTO;
import com.example.model.Account;
import com.example.model.CardDetail;

public interface IAccount {
	List<Account> accounts = new ArrayList<Account>();

//	List<Account> getAllAccounts();

	Account addAccount(Account account);

	public default List<Account> getAllAccount() {
		return new ArrayList<>();

	};

	public Account authorization(CardDetail cardDetails);

	public String otpGenerator();

	public Map<String, String> initiatePayment(MerchantReqDTO cardDetail);

}
