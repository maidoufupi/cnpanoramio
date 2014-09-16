package com.cnpanoramio.rest;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpanoramio.json.MessageResponse;
import com.cnpanoramio.json.PhotoResponse;
import com.cnpanoramio.json.MessageResponse.Message;
import com.cnpanoramio.service.MessageManager;
import com.cnpanoramio.service.MessageService;
import com.cnpanoramio.utils.UserUtil;

@Controller
@RequestMapping("/api/rest/message")
public class MessageRestService extends AbstractRestService {

	private transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private UserManager userManager; 
	
	@Autowired
	private MessageManager messageManager; 
	
	@Autowired
	private MessageService messageService; 
	
	@RequestMapping(value = "/{messageId}", method = RequestMethod.GET)
	@ResponseBody
	public MessageResponse getMessage(@PathVariable String messageId) {
		MessageResponse response = new MessageResponse();
		
		User me = null;
		try {
			me = UserUtil.getCurrentUser(userManager);
		}catch(Exception ex) {
		}
		
		response.setMessage(messageService.get(me, Long.parseLong(messageId)));
		response.setStatus(MessageResponse.Status.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public MessageResponse createMessage(@RequestBody Message message) {
		MessageResponse response = new MessageResponse();
		
		User me = UserUtil.getCurrentUser(userManager);
		
		// TODO debug
		ObjectWriter ow = new ObjectMapper().writer()
				.withDefaultPrettyPrinter();
		try {
			log.debug("create message: " + ow.writeValueAsString(message));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		response.setMessage(messageManager.addOrUpdate(me, message.getType(), message));
		response.setStatus(MessageResponse.Status.OK.name());
		return response;
	}
	
	@RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
	@ResponseBody
	public MessageResponse deleteMessage(@PathVariable String messageId) {
		MessageResponse response = new MessageResponse();
		
		User me = UserUtil.getCurrentUser(userManager);
		messageManager.deleteMessage(me.getId(), Long.parseLong(messageId));
		response.setStatus(MessageResponse.Status.OK.name());
		return response;
	}
	
	@RequestMapping(value = "/{messageId}/like", method = RequestMethod.GET)
	@ResponseBody
	public MessageResponse like(@PathVariable String messageId) {
		MessageResponse response = new MessageResponse();
		
		User me = UserUtil.getCurrentUser(userManager);
		messageManager.like(me, Long.parseLong(messageId));
		
		response.setStatus(MessageResponse.Status.OK.name());
		return response;
	}

	@RequestMapping(value = "/{messageId}/like", method = RequestMethod.DELETE)
	@ResponseBody
	public MessageResponse unLike(@PathVariable String messageId) {
		MessageResponse response = new MessageResponse();
		
		User me = UserUtil.getCurrentUser(userManager);
		messageManager.unLike(me, Long.parseLong(messageId));
		
		response.setStatus(MessageResponse.Status.OK.name());
		return response;
	}
	
	@RequestMapping(value = "/{messageId}/share", method = RequestMethod.POST)
	@ResponseBody
	public MessageResponse share(@PathVariable String messageId,
			@RequestBody Message message) {
		MessageResponse response = new MessageResponse();
		
		User me = UserUtil.getCurrentUser(userManager);
		
		response.setMessage(messageService.share(me, Long.parseLong(messageId), message.getContent()));
		
		response.setStatus(MessageResponse.Status.OK.name());
		return response;
	}
}
