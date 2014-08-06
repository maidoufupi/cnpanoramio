package com.cnpanoramio.json;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class AuthResponse extends ExceptionResponse {
	
	private UserOpenInfo user;
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public static class LoginRequest {
		public String username;
    	public String password;
    	
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public static class SignupCheckRes {
		public boolean isValid;
    	public String value;
		public boolean isValid() {
			return isValid;
		}
		public void setValid(boolean isValid) {
			this.isValid = isValid;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}   	
		
	}

	public UserOpenInfo getUser() {
		return user;
	}

	public void setUser(UserOpenInfo user) {
		this.user = user;
	}
	
	
}
