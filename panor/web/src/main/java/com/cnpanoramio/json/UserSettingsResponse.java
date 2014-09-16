package com.cnpanoramio.json;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UserSettingsResponse extends ExceptionResponse {
	
	private UserSettings settings;
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public static class Password {
		
		@JsonProperty("current_password")
		private String currentPassword;
		
		@JsonProperty("password")
		private String password;
		
		@JsonProperty("confirm_password")
		private String confirmPassword;
		
		public String getCurrentPassword() {
			return currentPassword;
		}
		public void setCurrentPassword(String currentPassword) {
			this.currentPassword = currentPassword;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getConfirmPassword() {
			return confirmPassword;
		}
		public void setConfirmPassword(String confirmPassword) {
			this.confirmPassword = confirmPassword;
		}
		
	}

	public UserSettings getSettings() {
		return settings;
	}

	public void setSettings(UserSettings settings) {
		this.settings = settings;
	}
	
	
}
