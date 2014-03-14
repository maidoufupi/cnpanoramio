package com.cnpanoramio.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.appfuse.model.LabelValue;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.json.PhotoProperties;
import com.cnpanoramio.json.UserOpenInfo;
import com.cnpanoramio.json.UserTransfer;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.UserSettingsManager;
import com.cnpanoramio.utils.UserUtil;

@Controller
@RequestMapping("/api/rest/user")
public class UserRestService {
	
	@Autowired
	private UserManager userManager = null; 
	
	@Autowired
	private UserSettingsManager userSettingsManager;
	
	@Autowired
	private PhotoManager photoService;
	
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authManager;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public UserTransfer getMe() {
		
		User me = UserUtil.getCurrentUser(userManager);
		Map<String, Boolean> roles = new HashMap<String, Boolean>();
		for (LabelValue labelValue : me.getRoleList()) {
			roles.put(labelValue.getValue(), Boolean.TRUE);
		}
		return new UserTransfer(me.getId(), me.getUsername(), true, roles);
	}
	
	@RequestMapping(value = "/{userId}/photos/{pageSize}/{pageNo}", method = RequestMethod.GET)
	@ResponseBody
	public Collection<PhotoProperties> getPhotos(
			@PathVariable String userId, 
			@PathVariable String pageSize,
			@PathVariable String pageNo) {
		
		int pageSizeI,
		    pageNoI;
		pageSizeI = Integer.valueOf(pageSize).intValue();
		pageNoI = Integer.valueOf(pageNo).intValue();
		
//		User me = UserUtil.getCurrentUser(userManager);
		
		Collection<PhotoProperties> photos = new ArrayList<PhotoProperties>();
		Collection<Photo> ps = photoService.getPhotosForUser(userId, pageSizeI, pageNoI);
		for (Photo photo : ps) {
			PhotoProperties pp = new PhotoProperties();
			pp.setId(photo.getId());
			pp.setTitle(photo.getTitle());
			pp.setDescription(photo.getDescription());
			photos.add(pp);
		}
		
		return photos;
	}
	
	@RequestMapping(value = "/{userId}/openinfo", method = RequestMethod.GET)
	@ResponseBody
	public UserOpenInfo getOpenInfo(@PathVariable String userId) {
		Long id = Long.parseLong(userId);
		return userSettingsManager.getOpenInfo(id);
	}
}
