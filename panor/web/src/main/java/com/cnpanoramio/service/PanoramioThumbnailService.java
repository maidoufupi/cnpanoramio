package com.cnpanoramio.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.cnpanoramio.service.json.BoundSize;
import com.cnpanoramio.service.json.PhotoThumbnails;

@Path("/panoramiothumbnail")
public interface PanoramioThumbnailService {

	@Produces("application/json")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED })
	public PhotoThumbnails getPhotoThumbnails(BoundSize boundSize);
}
