package com.cnpanoramio.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cnpanoramio.json.MessageResponse.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(inheritLocations = true,
        locations = { 
				"classpath:/applicationContext-resources.xml",
                "classpath:/applicationContext-service.xml",
                "classpath:/applicationContext-dao.xml",
                "classpath*:/applicationContext.xml", // for modular archetypes
                "classpath*:/applicationContext-test.xml",
                "/WEB-INF/spring-security.xml",
                "/WEB-INF/applicationContext*.xml",
                "/WEB-INF/dispatcher-servlet.xml"})
public class MessageManagerTest {

	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private MessageManager messageManager;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private UserManager userManager;
	
	private Long userId;
	
	@Before
	public void preMethodSetup() {
		userId = 3L;
	}

	@After
	public void postMethodTearDown() {
	}

	@Test
	public void testGetMessages() {
		User user = userManager.get(userId);
		List<Message> messages = messageService.getMessages(user, 10, 1);
		log.info(messages.size());
		for(com.cnpanoramio.json.MessageResponse.Message message : messages) {
			log.info(message.getId());
		}
		
	}
}
