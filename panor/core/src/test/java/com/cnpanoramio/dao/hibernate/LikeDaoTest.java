package com.cnpanoramio.dao.hibernate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.cnpanoramio.dao.LikeDao;
import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.domain.Comment;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.UserSettings;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:/applicationContext.xml" //"classpath*:/applicationContext-resources.xml",
		})
@Transactional
public class LikeDaoTest {

	protected transient final Log log = LogFactory.getLog(getClass());
	
	private Photo photo;
	private Comment comment;
	
	@Autowired
	private PhotoDao photoDao;
	@Autowired
	private LikeDao likeDao;
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private CommentDao commentDao;
	
	private User user;
	
	@Before
	public void preMethodSetup() {
		user = userDao.get(1L);
		
	}

	@After
	public void postMethodTearDown() {
		
	}

	@Test
	public void testLikeComment() {
		comment = commentDao.get(1L);
		likeDao.likeComment(comment, user);
		
		comment = commentDao.get(1L);
		Assert.assertEquals(1, comment.getLikes().size());
	}	
	
}
