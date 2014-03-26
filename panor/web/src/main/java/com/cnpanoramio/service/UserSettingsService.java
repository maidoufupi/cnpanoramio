package com.cnpanoramio.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.cnpanoramio.domain.UserSettings;

@Path("/settings")
public interface UserSettingsService {

	@Produces("application/json")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	public UserSettings save(UserSettings userSettings);
	
	
}
