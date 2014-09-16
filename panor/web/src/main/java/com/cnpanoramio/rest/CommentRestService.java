package com.cnpanoramio.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpanoramio.dao.CommentDao;
import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.json.CommentResponse;
import com.cnpanoramio.json.CommentResponse.Comment;
import com.cnpanoramio.service.CommentService;
import com.cnpanoramio.service.LikeManager;
import com.cnpanoramio.utils.UserUtil;

@Controller
@RequestMapping("/api/rest/comment")
public class CommentRestService extends AbstractRestService {

	private transient final Log log = LogFactory.getLog(getClass());

	@Autowired
	private UserManager userManager; 
	
	@Autowired
	private PhotoDao photoDao;
	
	@Autowired
	private CommentDao commentDao;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private LikeManager likeManager;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public CommentResponse saveComment(@RequestBody Comment comment) {
		CommentResponse response = CommentResponse.getInstance();
		
		User me = UserUtil.getCurrentUser(userManager);
		
		response.setComment(commentService.save(me, comment));
		response.setStatus(CommentResponse.Status.OK.name());
		return response;
	}
	
	@RequestMapping(value = "/{commentId}", method = RequestMethod.DELETE)
	@ResponseBody
	public CommentResponse deleteComment(@PathVariable String commentId) {
		CommentResponse response = CommentResponse.getInstance();
		
		Long id = Long.parseLong(commentId);
		if(commentService.delete(id)) {
			response.setStatus(CommentResponse.Status.OK.name());
		}else {
			response.setStatus(CommentResponse.Status.EXCEPTION.name());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/{commentId}", method = RequestMethod.POST)
	@ResponseBody
	public CommentResponse modifyComment(@PathVariable String commentId, @RequestParam("content") String content) {
		
		CommentResponse response = CommentResponse.getInstance();
		response.setStatus(CommentResponse.Status.OK.name());

		commentService.modify(Long.parseLong(commentId), content);		
		return response;
	}
	
	@RequestMapping(value = "/{commentId}/like", method = RequestMethod.GET)
	@ResponseBody
	public CommentResponse likeComment(@PathVariable String commentId) {
		
		CommentResponse response = CommentResponse.getInstance();
		response.setStatus(CommentResponse.Status.OK.name());

		User me = UserUtil.getCurrentUser(userManager);
		likeManager.likeComment(me, Long.parseLong(commentId));
		
		return response;
	}
	
	@RequestMapping(value = "/{commentId}/like", method = RequestMethod.DELETE)
	@ResponseBody
	public CommentResponse unLikeComment(@PathVariable String commentId) {
		
		CommentResponse response = CommentResponse.getInstance();
		response.setStatus(CommentResponse.Status.OK.name());
		
		User me = UserUtil.getCurrentUser(userManager);
		likeManager.likeComment(me, Long.parseLong(commentId));
		
		return response;
	}
}
