package com.cnpanoramio.webapp.oauth;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

@Path("/token")
public interface ClientTokenService {
	
	/**
	 * Retrieve an access token stored against the provided authentication key, if it exists.
	 * 
	 * @param authentication the authentication key for the access token
	 * 
	 * @return the access token or null if there was none
	 */
	public OAuth2AccessToken getAccessToken(HttpServletRequest userName);
}
