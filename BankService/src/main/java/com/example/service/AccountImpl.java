package com.example.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.databaseConnection.ExcelDataHandler;
import com.example.dto.MerchantReqDTO;
import com.example.model.Account;
import com.example.model.CardDetail;

@Service
public class AccountImpl implements IAccount {

	@Autowired
	ExcelDataHandler excelDataHandler;
//	@Autowired
//	CardDetail cardDetails;

	private static final float cardLimit = 10000;

	Logger logger = LogManager.getLogger(getClass().getName());

	@Override
	public Account addAccount(Account account) {
		accounts.add(account);
		try {
			excelDataHandler.appendData(accounts);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return account;
	}

	@Override
	public List<Account> getAllAccount() {

		List<Account> list = new ArrayList<>();
		try {
			list = excelDataHandler.readData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<>(list);
	}

	@Override
	public Account authorization(CardDetail cardDetail) {
		List<Account> list = new ArrayList<>();
		try {
			list = excelDataHandler.readData();
			Account Account = list.stream()
					.filter(account -> new String(Base64.getDecoder().decode(account.cardDetails.getCardNo()))
							.equals(cardDetail.getCardNo()))
					.findAny().orElse(null);
			return Account;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String otpGenerator() {
		StringBuilder generatedOTP = new StringBuilder();
		SecureRandom secureRandom = new SecureRandom();
		try {
			secureRandom = SecureRandom.getInstance(secureRandom.getAlgorithm());
			for (int i = 0; i < 6; i++) {
				generatedOTP.append(secureRandom.nextInt(9));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedOTP.toString();

	}

	@Override
	public Map<String, String> initiatePayment(MerchantReqDTO cardDetail) {
		String paymentStatus = null;
		List<Account> list = new ArrayList<>();
		Map<String, String> status = new HashMap<>();
		try {
			if (cardLimit < cardDetail.getAmount()) {
				status.put("cardLimit", "exceeds");
				status.put("status", "unsuccessful");
				status.put("Initiate", "notInitiated");
				return status;
			} else {
				list = excelDataHandler.readData();
				Account userAccount = list.stream()
						.filter(account -> new String(Base64.getDecoder().decode(account.cardDetails.getCardNo()))
								.equals(cardDetail.getCardDetail().getCardNo()))
						.findAny().orElse(null);
				if (userAccount.getBalance() > cardDetail.getAmount()) {
					paymentStatus = excelDataHandler.subtractAmount(userAccount, cardDetail.getAmount());
					if (paymentStatus != null) {
						status.put("status", "success");
					}
					status.put("Initiate", "initiated");
					return status;
				} else {
					logger.info("insufficient balance");
					status.put("balance", "insufficient balance");
					status.put("status", "unsuccessful");
					status.put("Initiate", "notInitiated");
				}
				return status;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
