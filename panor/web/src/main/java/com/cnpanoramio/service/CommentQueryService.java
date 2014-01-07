package com.cnpanoramio.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.cnpanoramio.service.json.Comment;
import com.cnpanoramio.service.json.CommentQueryInput;
import com.cnpanoramio.service.json.CommentResult;

@Path("/commentquery")
public interface CommentQueryService {
		
	@Produces("application/json")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	public List<CommentResult> getComments(CommentQueryInput input);
	
	@Produces("application/json")
	@GET
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("{photoId}")
	public Integer getCommentSize(@PathParam(value="photoId") Long photoId);
}
