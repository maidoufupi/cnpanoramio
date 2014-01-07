package com.cnpanoramio.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.cnpanoramio.dao.UserSettingsDao;
import com.cnpanoramio.domain.Comment;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.service.CommentQueryService;
import com.cnpanoramio.service.CommentService;
import com.cnpanoramio.service.json.CommentQueryInput;
import com.cnpanoramio.service.json.CommentResult;

@Service("commentqueryService")
@Transactional
public class CommentQueryServiceImpl implements CommentQueryService {

	private transient final Log log = LogFactory.getLog(CommentQueryService.class);
	
	@Autowired
	private UserManager userManager;
	@Autowired
	private PhotoDao photoDao;
	@Autowired
	private CommentDao commentDao;
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public List<CommentResult> getComments(CommentQueryInput input) {
		log.info(input.getPhotoId() + input.getPageNo() + input.getPageSize());
		
		List<Comment> comments = commentDao.getCommentPager(input.getPhotoId(), input.getPageNo(), input.getPageSize());
		List<CommentResult> cs = new ArrayList<CommentResult>();
		for(Comment comment : comments) {
			CommentResult c1 = new CommentResult();
			c1.setId(comment.getId());
			c1.setUserid(comment.getUser().getId());
			c1.setUsername(comment.getUser().getUsername());
			c1.setCreateTime(format.format(comment.getCreateTime().getTime()));
			c1.setComment(comment.getComment());
			cs.add(c1);
		}
		return cs;
	}
	
	@Override
	public Integer getCommentSize(Long photoId) {
		return commentDao.getCommentSize(photoId).intValue();
	}

}
