package com.cnpanoramio.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpanoramio.dao.CommentDao;
import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.json.Comment;
import com.cnpanoramio.json.PhotoComments;
import com.cnpanoramio.service.CommentService;

@Controller
@RequestMapping("/api/rest/comment")
public class CommentRestService {

	private transient final Log log = LogFactory.getLog(getClass());

	@Autowired
	private UserManager userManager; 
	
	@Autowired
	private PhotoDao photoDao;
	
	@Autowired
	private CommentDao commentDao;
	
	@Autowired
	private CommentService commentService;
	
	@RequestMapping(value = "/photo/{photoId}", method = RequestMethod.POST)
	@ResponseBody
	public Comment saveComment(@PathVariable String photoId, @RequestBody Comment comment) {
		
		comment.setPhotoId(Long.parseLong(photoId));
		return commentService.save(comment);

	}
	
	@RequestMapping(value = "/photo/{photoId}/{commentId}", method = RequestMethod.DELETE)
	@ResponseBody
	public boolean deleteComment(@PathVariable String photoId, @PathVariable String commentId) {
		
		Long id = Long.parseLong(commentId);
		return commentService.delete(id);
		
	}

	@RequestMapping(value = "/photo/{photoId}", method = RequestMethod.GET)
	@ResponseBody
	public PhotoComments getCount(@PathVariable String photoId) {

		Long id = 0L;
		id = Long.parseLong(photoId);

		PhotoComments pc = new PhotoComments();
		pc.setId(id);
		pc.setCount(commentService.getCount(id).intValue());

		return pc;
	}

	@RequestMapping(value = "/photo/{photoId}/{pageSize}/{pageNo}", method = RequestMethod.GET)
	@ResponseBody
	public PhotoComments getComments(@PathVariable String photoId,
			@PathVariable String pageSize, @PathVariable String pageNo) {
		
		Long photoIdL = Long.parseLong(photoId);
		int pageSizeL = Integer.parseInt(pageSize);
		int pageNoL = Integer.parseInt(pageNo);		
		
		return commentService.getComments(photoIdL, pageSizeL, pageNoL);
	}
}
