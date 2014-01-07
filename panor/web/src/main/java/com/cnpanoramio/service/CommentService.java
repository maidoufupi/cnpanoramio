package com.cnpanoramio.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.cnpanoramio.service.json.Comment;
import com.cnpanoramio.service.json.CommentResult;

@Path("/comment")
public interface CommentService {
	
	@Produces("application/json")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	public CommentResult save(Comment comment);
	
	
}
