package com.example.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Configuration
public class UserDetailsImpl implements UserDetailsService{
	
	@Value("${spring.security.user.name}")
	private String userName;
	
	@Value("${spring.security.user.password}")
	private String password;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {		
		
		if(username.equals(userName))
		{
			return new User(userName, password, new ArrayList<>());
		}
		else {
			throw new UsernameNotFoundException("access denied");
		}
	}

}
