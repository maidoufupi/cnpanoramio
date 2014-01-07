package com.cnpanoramio.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.ws.addressing.wsdl.Anonymous;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.CommentDao;
import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.service.CommentService;
import com.cnpanoramio.service.json.Comment;
import com.cnpanoramio.service.json.CommentResult;

@Service("commentService")
@Transactional
public class CommentServiceImpl implements CommentService {

	private transient final Log log = LogFactory.getLog(CommentService.class);
	
	@Autowired
	private UserManager userManager;
	@Autowired
	private PhotoDao photoDao;
	@Autowired
	private CommentDao commentDao;
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public CommentResult save(Comment comment) {
		User user = null;
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if(null != auth) {
			Object principal = auth.getPrincipal();
			String username;
			if (principal instanceof UserDetails) {
				username = ((UserDetails) principal).getUsername();
			} else {
				username = principal.toString();
			}
			user = userManager.getUserByUsername(username);
		}
		com.cnpanoramio.domain.Comment commentd = new com.cnpanoramio.domain.Comment();
		commentd.setCreateTime(Calendar.getInstance());
		commentd.setComment(comment.getComment());
		commentd.setUser(user);
		
		Photo photo = photoDao.get(comment.getPhotoid());
		commentd.setPhoto(photo);
		commentd = commentDao.save(commentd);
		
		CommentResult cResult = new CommentResult();
		cResult.setId(commentd.getId());
		cResult.setUserid(commentd.getUser().getId());
		cResult.setUsername(commentd.getUser().getUsername());
		cResult.setCreateTime(format.format(commentd.getCreateTime().getTime()));
		cResult.setComment(commentd.getComment());
		return cResult;
	}

	
}
