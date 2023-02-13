package com.example.controller;

import java.io.IOException;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.service.UserDetailsImpl;
import com.example.utils.JwtUtil;
import com.example.dto.JwtRequest;
import com.example.dto.MerchantReqDTO;
import com.example.model.CardDetail;
import com.example.model.PaymentRecord;
import com.example.service.PaymentImpl;

@RestController
public class PaymentServiceController {
//	@Autowired
//	Account account;
	@Autowired
	PaymentImpl paymentService;
	
	@Autowired
	private UserDetailsImpl userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;

	RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private JavaMailSender emailSender;

	@Value("${spring.mail.username}")
	private String host;

	UUID uid = null;
	List result = new ArrayList<>();

	private static boolean isAuthorized = false;

	protected static MerchantReqDTO cardDetail;

	Logger logger = LogManager.getLogger(getClass().getName());
	
	String token = null;
	
	@PostMapping("/Jwt")
	public String generateToken(@RequestBody JwtRequest jwtRequest) throws Exception
	{
		
		try {
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),jwtRequest.getPassword()));
			
		} catch (UsernameNotFoundException e) {
			e.printStackTrace();
			throw new Exception("Username Not found");
		}catch (BadCredentialsException e) {
			e.printStackTrace();
			throw new Exception("Bad Credentials");
		}
		UserDetails userDetails = this.userService.loadUserByUsername(jwtRequest.getUsername());

		String token = this.jwtUtil.generateToken(userDetails);

//		    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));        
//		    headers.set("User-Agent", "Spring's RestTemplate" );  // value can be whatever
//		    headers.set("Authorization", "Bearer "+token );
//		    System.out.println("get token= "+headers.get("Authorization"));		
		
		return token;
	}
	

	@PostMapping("/Authorize")
	public ResponseEntity<String> Authentication(@RequestBody MerchantReqDTO cardDetail)
			throws IOException, InterruptedException {
		logger.info("Card Authentication");
		String uri = "http://localhost:8080/authentication";
		this.cardDetail = cardDetail;
		try {
			if (paymentService.CheckAuthorized(cardDetail.cardDetail)) {
				result = restTemplate.postForObject(uri, cardDetail, List.class);
				logger.info("Result: " + result);
				if (!result.isEmpty()) {
					isAuthorized = true;
					uid = paymentService.generateTranId();
					cardDetail.cardDetail.setCardNo(
							Base64.getEncoder().encodeToString(cardDetail.getCardDetail().getCardNo().getBytes()));
					boolean res = paymentService.Update(new PaymentRecord(cardDetail.getCardDetail().getCardNo(),
							uid.toString(), "NotInitiate", "Authorize", "Unsuccessful"));
					logger.info("Complete Card Authentication");
					return new ResponseEntity<>("Authorized Card", HttpStatus.OK);
				} else {
					isAuthorized = false;
					cardDetail.cardDetail.setCardNo(
							Base64.getEncoder().encodeToString(cardDetail.getCardDetail().getCardNo().getBytes()));
					boolean res = paymentService.Update(new PaymentRecord(cardDetail.getCardDetail().getCardNo(),
							"null", "NotInitiate", "Unauthorize", "Unsuccessful"));
					logger.info("Card Authentication Failed!!");
					return new ResponseEntity<>("Unauthorize card", HttpStatus.UNAUTHORIZED);
				}
			} else {
				System.out.println(paymentService.CheckAuthorized(cardDetail.cardDetail));
			}
		} catch (Exception e) {
			logger.error(e);
			return new ResponseEntity<>("Unauthorize card", HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<>("Unauthorize card", HttpStatus.UNAUTHORIZED);
	}

	@GetMapping("/allRecord")
	public ResponseEntity<Object> getAccountEntity(HttpServletResponse response) {
		logger.info("Retriving All Payment Details ");
		try {
			List<PaymentRecord> list = paymentService.record();
			return new ResponseEntity<Object>(list, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e);
		}
		return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@GetMapping("/verify/{otp}")
	public ResponseEntity<Map<String, String>> verifyOTP(@PathVariable String otp) {
		logger.info("Verifing otp ");
		String URI = otp;
		Map<String, String> msg = new HashMap<>();
		try {
			msg = restTemplate.getForObject("http://localhost:8080/verifyotp/{otp}", Map.class, URI);

			if (msg != null && result != null) {
				paymentService.updateInfo(new PaymentRecord(cardDetail.getCardDetail().getCardNo(), uid.toString(),
						msg.get("Initiate"), "Authorize", msg.get("status")));
				logger.info("Payment details udated");
				return new ResponseEntity<>(msg, HttpStatus.OK);
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	@GetMapping("/getotp/{email}")
	public ResponseEntity<String> sendOTP(@PathVariable String email) {
		String URI = email;
		logger.info("Sending otp over email");
		try {
			if (isAuthorized) {
				String msg = restTemplate.getForObject("http://localhost:8080/getotp/{email}", String.class, URI);
				return new ResponseEntity<String>(msg, HttpStatus.OK);
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return new ResponseEntity<String>("Payment Unsuccessful", HttpStatus.UNAUTHORIZED);
	}

}
