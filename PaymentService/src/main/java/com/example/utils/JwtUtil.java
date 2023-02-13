package com.example.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//method-for generating token//
@Component
public class JwtUtil implements Serializable {

	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // 5 hours
	private static final long serialVersionUID = -2550185165626007488L;
	// @Value("${jwt.secret}") // SpEL - jwt.secret in application.properties
	private String secret = "Amruta";

	// check if the token has expired
	private Boolean isTokenExpired(String token) {
		// parsing the expiry date from token
		final Date expiration = getExpirationDateFromToken(token);
		// comparing the expiry date and time with current date and time
		return expiration.before(new Date());
	}

	// retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	// retrieve username from jwt token
	public String extractEmailFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	// ----------------- CLAIMS FROM TOKEN ----------------------
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	// for retrieving any information from token we will need the secret key
	public Claims getAllClaimsFromToken(String token) {
		// claims contain userDetails, token expiry, iat
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	// --------------------------------------------------------


    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }
	// generate token for user : not used currently, can be used when we need to
	// pass id
	public String generateToken(Integer id) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, id.toString());
	}

	public String generateToken(String email) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, email);
	}

	// while creating the token -
	// 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
	// 2. Sign the JWT using the SHA256 algorithm and secret key.
	// 3. According to JWS Compact
	// Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	// compaction of the JWT to a URL-safe string
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				// passing empty map to set all the values in token as claims
				.setClaims(claims)
				// subject consists of user data : email
				.setSubject(subject)
				// setting current time as issue date and time
				.setIssuedAt(new Date(System.currentTimeMillis()))
				// set expiry of token
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 10000000))
				// set encryption type and pass "secret key" to encrypt or decrypt data
				// same secret key must be used to parse the token
				.signWith(SignatureAlgorithm.HS256, secret)
				// xxx.yyy.zzz
				.compact();

		// xxx : Header : consist type of the token - JWT and signing algorithm: HMAC
		// SHA256 or RSA.
		// yyy : Payload : contains claims
		// zzz : Signature : take both encoded header, payload and secret and sign that

	}

	// validate token : not used currently
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractEmailFromToken(token);
		System.out.println(username+" "+userDetails.getUsername());
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	// validate token : used this method
	public Boolean validateToken(String token) {
		return !isTokenExpired(token);
	}
}


