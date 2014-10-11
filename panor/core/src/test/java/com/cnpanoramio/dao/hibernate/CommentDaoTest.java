package com.cnpanoramio.dao.hibernate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.UserDao;
import org.appfuse.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.CommentDao;
import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.domain.Comment;
import com.cnpanoramio.domain.Photo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:/applicationContext.xml" //"classpath*:/applicationContext-resources.xml",
		})
@Transactional
public class CommentDaoTest {

	protected transient final Log log = LogFactory.getLog(getClass());
	
	Photo photo;
	Comment comment;
	
	@Autowired
	PhotoDao photoDao;
	@Autowired
	CommentDao commentDao;
	@Autowired
	UserDao userDao;
	
	User user;
	
	@Before
	public void preMethodSetup() {
		user = userDao.getUsers().get(0);
		photo = new Photo();
		
		photo.setOwner(user);
		photoDao.save(photo);
		
		comment = new Comment();
//		comment.setPhoto(photo);
		comment.setUser(userDao.getUsers().get(1));
		comment.setContent("Very good");
		comment.setCreateDate(new Date());
//		comment.setCreateTime(Calendar.getInstance());
		comment = commentDao.save(comment);
		comment = commentDao.save(comment);
		
		comment = new Comment();
//		comment.setCreateTime(Calendar.getInstance());
//		comment.setPhoto(photo);
		comment.setUser(userDao.getUsers().get(1));
		comment.setContent("Very good 2");
		comment.setCreateDate(new Date());
		comment = commentDao.save(comment);
	}

	@After
	public void postMethodTearDown() {
		commentDao.remove(comment);
		photoDao.remove(photo);
	}

	@Test
	public void testGetPhotoComments() {
		List<Comment> comments = commentDao.getComments(photo);
		Assert.assertTrue(comments.size() == 1);
		log.info(comments.get(0).getComment());
//		log.info(comments.get(0).getPhoto().getOwner().getUsername());
		log.info(comments.get(0).getUser().getUsername());
	}
	
	@Test
	public void testGetCommentsPager() {
		Assert.assertEquals(commentDao.getCommentSize(photo.getId()).intValue(), 2);
		List<Comment> comments = commentDao.getComments(photo);
		Assert.assertTrue(comments.size() == 1);
//		log.info(comments.get(0).getCreateTime().get(Calendar.YEAR));
//		log.info(comments.get(0).getCreateTime().get(Calendar.MONTH));
//		log.info(comments.get(0).getCreateTime().get(Calendar.DATE));
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
//		log.info(format1.format(comments.get(0).getCreateTime().getTime()));
		log.info(comments.get(0).getComment());
//		log.info(comments.get(0).getPhoto().getOwner().getUsername());
		log.info(comments.get(0).getUser().getUsername());
	}
	
}
