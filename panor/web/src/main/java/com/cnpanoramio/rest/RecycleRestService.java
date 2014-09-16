package com.cnpanoramio.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpanoramio.json.MessageResponse;
import com.cnpanoramio.json.RecycleResponse;
import com.cnpanoramio.json.UserResponse;
import com.cnpanoramio.service.RecycleManager;
import com.cnpanoramio.service.RecycleService;
import com.cnpanoramio.utils.UserUtil;

@Controller
@RequestMapping("/api/rest/recycle")
public class RecycleRestService extends AbstractRestService {

	private transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private UserManager userManager; 
	
	@Autowired
	private RecycleManager recycleManager;
	
	@Autowired
	private RecycleService recycleService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public RecycleResponse getRecycleBin() {
		RecycleResponse response = new RecycleResponse();
		
		User me = UserUtil.getCurrentUser(userManager);
		response.setRecycles(recycleService.getUserRecycleBin(me));
		
		response.setStatus(RecycleResponse.Status.OK);
		return response;
	}
	
	@RequestMapping(value = "", method = RequestMethod.DELETE)
	@ResponseBody
	public RecycleResponse emptyRecycleBin(@PathVariable String userId) {
		RecycleResponse response = new RecycleResponse();

		User me = UserUtil.getCurrentUser(userManager);
		recycleService.emptyRecycleBin(me, Long.parseLong(userId));
		
		response.setStatus(RecycleResponse.Status.OK);
		return response;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public RecycleResponse removeRecycle(@PathVariable String id) {
		RecycleResponse response = new RecycleResponse();

		User me = UserUtil.getCurrentUser(userManager);
		recycleService.removeRecycle(me, Long.parseLong(id));
		
		response.setStatus(RecycleResponse.Status.OK);
		return response;
	}
	
	@RequestMapping(value = "/{id}/cancel", method = RequestMethod.GET)
	@ResponseBody
	public RecycleResponse cancelRecycle(@PathVariable String id) {
		RecycleResponse response = new RecycleResponse();
		
		User me = UserUtil.getCurrentUser(userManager);
		recycleService.cancelRecycle(me, Long.parseLong(id));
		
		response.setStatus(RecycleResponse.Status.OK);
		return response;
	}
}
