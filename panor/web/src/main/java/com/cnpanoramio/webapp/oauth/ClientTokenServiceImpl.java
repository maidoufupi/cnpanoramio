package com.cnpanoramio.webapp.oauth;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

public class ClientTokenServiceImpl implements ClientTokenService {

	DefaultTokenServices tokenServices;
	
	String userName = "username";
	
	public OAuth2AccessToken getAccessToken(@Context HttpServletRequest request) {
		
		Collection<OAuth2AccessToken> tokens = tokenServices.findTokensByUserName(request.getAttribute(userName).toString());
		
		for(OAuth2AccessToken token : tokens) {
			
		}
		return null;
	}

	@Autowired
	public void setTokenServices(DefaultTokenServices tokenServices) {
		this.tokenServices = tokenServices;
	}

}
