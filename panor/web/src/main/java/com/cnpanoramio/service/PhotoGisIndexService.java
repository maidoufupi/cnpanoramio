package com.cnpanoramio.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/gisindex")
public interface PhotoGisIndexService {

	@GET
	public boolean updateGisIndex();
}
