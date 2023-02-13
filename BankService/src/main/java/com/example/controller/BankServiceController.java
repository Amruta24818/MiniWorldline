package com.example.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.MerchantReqDTO;
import com.example.dto.ResponseDTO;
import com.example.model.Account;
import com.example.model.CardDetail;
import com.example.service.AccountImpl;

@RestController
public class BankServiceController {
//	@Autowired
//	Account account;
	@Autowired
	AccountImpl accountService;

	@Autowired
	private JavaMailSender emailSender;

	@Value("${spring.mail.username}")
	private String host;

	private static String otp;

	protected static MerchantReqDTO cardDetail;

	Logger logger = LogManager.getLogger(getClass().getName());

	@PostMapping("/authentication")
	public ResponseEntity<List<String>> AuthenticateCardDetails(@RequestBody MerchantReqDTO cardDetail,
			HttpServletRequest request) {
		logger.info("Inside Authentication");
		this.cardDetail = cardDetail;
		List<String> list = new ArrayList<>();
		try {
			Account account = accountService.authorization(cardDetail.getCardDetail());
			if (account != null) {
				list.add("Authorized");
				logger.info("Complete Card Authentication");
				return new ResponseEntity<>(list, HttpStatus.OK);

			}
			list.add("Unauthorized");
			logger.debug("Card Authentication Failed!!");
			return new ResponseEntity<>(list, HttpStatus.UNAUTHORIZED);

		} catch (Exception e) {
			logger.error(e);
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping("/addAccount")
	public ResponseEntity<Object> addAccountEntity(@RequestBody Account account, HttpServletRequest request) {
		logger.info("Adding account");
		try {
			account.cardDetails
					.setCardNo(Base64.getEncoder().encodeToString(account.cardDetails.getCardNo().getBytes()));
			account.cardDetails.setCvv(Base64.getEncoder().encodeToString(account.cardDetails.getCvv().getBytes()));
			logger.info("Account added Successfully");
			return new ResponseEntity<Object>(accountService.addAccount(account), HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/allAccount")
	public ResponseEntity<Object> getAccountEntity(HttpServletResponse response) {
		logger.info("Retriving all accounts info");
		try {
			List<Account> list = accountService.getAllAccount();

			return new ResponseEntity<Object>(list, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/getotp/{email}")
	public ResponseEntity<String> sendOTP(@PathVariable String email) {

		return new ResponseEntity<String>(OTPEmail(email), HttpStatus.OK);
	}

	public String OTPEmail(String email) {
		try {
			System.out.println("Sending Email.....");
			logger.info("Sending Email.....");
			otp = "" + ((int) (Math.random() * 9000) * 100);
//			System.out.println("OTP: " + otp);
			SimpleMailMessage mesg = new SimpleMailMessage();
			mesg.setFrom(host);
			mesg.setTo(email);
			mesg.setSubject("Welcome to MiniWorldLine");
			mesg.setText("Hello,\nYour OTP for Payment is  " + otp + "\n\n\nThanks and Regards,\nAdmin\nMiniWorldLine");
			emailSender.send(mesg);

			System.out.println("success");
			logger.info("OTP has been sent");
			return "OTP has been sent";
		} catch (Exception e) {
			logger.error(e);
			return "OTP has not been sent";
		}

	}

	@GetMapping("/verifyotp/{userotp}")
	public ResponseEntity<Map<String, String>> verifyOTP(@PathVariable String userotp) {

		return new ResponseEntity<Map<String, String>>(OTPverify(userotp), HttpStatus.OK);
	}

	private Map<String, String> OTPverify(String userotp) {
		try {
			logger.info("Verifying otp");
			Map<String, String> status = new HashMap<>();
			if (otp.compareTo(userotp) == 0) {
				// initiate payment
				status = accountService.initiatePayment(cardDetail);
				logger.info("Payment Initiated");
				return status;
			}

		} catch (Exception e) {
			logger.error(e);
			return null;
		}
		return null;

	}
}
