package com.example.dto;

public class JwtResponce {
	
	    private String token;

	    public  JwtResponce() {
	        System.out.println("\n----------- CTOR: LoginResponse default CTOR --------------\n");
	    }

	    public  JwtResponce( String token) {
	        System.out.println("\n----------- CTOR: LoginResponse - generating response with user details and JWT --------------\n");
	   
	        this.token = token;
	    }
	    

	    public String getToken() {
	        return token;
	    }

	    public void setToken(String token) {
	        this.token = token;
	    }

	    @Override
	    public String toString() {
	        return "LoginResponse [token=" + token + "]";
	    }


}
