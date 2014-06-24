package com.cnpanoramio.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.CommentDao;
import com.cnpanoramio.dao.LikeDao;
import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.domain.Favorite;
import com.cnpanoramio.domain.Like;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.json.CommentResponse.Comment;
import com.cnpanoramio.json.UserResponse.Settings;
import com.cnpanoramio.service.CommentService;
import com.cnpanoramio.service.UserSettingsManager;
import com.cnpanoramio.utils.UserUtil;

@Service("commentService")
@Transactional
public class CommentServiceImpl implements CommentService {

	private transient final Log log = LogFactory.getLog(CommentService.class);
	
	@Autowired
	private UserManager userManager;
	@Autowired
	private UserSettingsManager userSettingsManager;
	@Autowired
	private PhotoDao photoDao;
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private LikeDao likeDao;
	
	@Override
	public Comment save(Comment comment) {
		
		User me = UserUtil.getCurrentUser(userManager);
		
		com.cnpanoramio.domain.Comment commentD = new com.cnpanoramio.domain.Comment();
		commentD.setComment(comment.getContent());
		commentD.setCreateTime(Calendar.getInstance());
		commentD.setUser(me);
		Photo photo = photoDao.get(comment.getPhotoId());
		commentD.setPhoto(photo);
		commentD = commentDao.save(commentD);
		photo.getComments().add(commentD);
		
		Settings settings = userSettingsManager.getCurrentUserSettings();
		comment.setId(commentD.getId());
		// 昵称
		comment.setUsername(settings.getName());
		comment.setCreateTime(commentD.getCreateTime().getTime());
		comment.setUserId(commentD.getUser().getId());
		comment.setUserAvatar(settings.getUserAvatar());
		return comment;		
	}
	
	public boolean delete(Long id) {		
		
		User me = UserUtil.getCurrentUser(userManager);
		com.cnpanoramio.domain.Comment commentD = commentDao.get(id);
		if(commentD.getUser().getId() == me.getId()) {
			commentD.getPhoto().getComments().remove(commentD);
			commentDao.remove(commentD);
			return true;
		}
		
		return false;
	}

	public Collection<Comment> getComments(Long id, int pageSize, int pageNo, User user) {
		List<com.cnpanoramio.domain.Comment> comments = commentDao.getCommentPager(id, pageSize, pageNo);
		
		List<Comment> cs = new ArrayList<Comment>();
		for(com.cnpanoramio.domain.Comment comment : comments) {
			Comment c1 = convertComment(comment);
			if(null != user) {
				Like like = likeDao.getComment(comment, user);
				if(null != like) {
					c1.setLike(true);
				}
			}
			cs.add(c1);
		}
		
		return cs;
	}
	
	public Long getCount(Long id) {
		return commentDao.getCommentSize(id);
	}

	@Override
	public Comment modify(Long id, String content) {
		com.cnpanoramio.domain.Comment comment = commentDao.get(id);
		comment.setComment(content);
		return convertComment(comment);
	}
	
	public Comment convertComment(com.cnpanoramio.domain.Comment commentD) {
		Comment comment = new Comment();
		UserSettings settings = userSettingsManager.get(commentD.getUser().getId());
		comment.setId(commentD.getId());
		if(null != settings.getAvatar()) {
			comment.setUserAvatar(settings.getAvatar().getId());
		}else {
			comment.setUserAvatar(1L);
		}
		
		// 昵称
		comment.setUsername(settings.getName());
		comment.setCreateTime(commentD.getCreateTime().getTime());
		comment.setUserId(commentD.getUser().getId());
		comment.setContent(commentD.getComment());
		if(null != commentD.getPhoto()) {
			comment.setPhotoId(commentD.getPhoto().getId());
		}
		
		comment.setLikeCount(commentD.getLikes().size());
		
		return comment;
	}
}
