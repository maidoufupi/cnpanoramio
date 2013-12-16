package com.cnpanoramio.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cnpanoramio.service.json.BoundSize;
import com.cnpanoramio.service.json.PhotoThumbnails;

@Path("/panoramiothumbnail")
public interface PanoramioThumbnailService {

	@Produces("application/json")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED })
	public PhotoThumbnails getPhotoThumbnails(BoundSize boundSize);
	
	@Produces("image/*")
	@GET
	@Consumes({ MediaType.APPLICATION_JSON})
	public Response read(Long photoId);
	
}
