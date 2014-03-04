package com.cnpanoramio.json;

import java.util.Map;


public class UserTransfer {
	
	private final Long id;
	
	private final String name;
	
	private final boolean loggedIn;

	private final Map<String, Boolean> roles;

//	private final String token;


	public UserTransfer(Long id, String userName, boolean loggedIn, Map<String, Boolean> roles) {
		
		this.id = id;
		this.name = userName;
		this.loggedIn = loggedIn;
		this.roles = roles;
//		this.token = token;
	}


	public String getName() {

		return this.name;
	}


	public Map<String, Boolean> getRoles() {

		return this.roles;
	}


//	public String getToken() {
//
//		return this.token;
//	}
	
	public boolean isLoggedIn() {
		return loggedIn;
	}


	public Long getId() {
		return id;
	}
	
	

}
