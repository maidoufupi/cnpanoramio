package com.cnpanoramio.webapp.rest;

import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.json.PhotoCameraInfo;
import com.cnpanoramio.json.PhotoProperties;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.utils.UserUtil;

@Controller
@RequestMapping("/api/rest/photo")
public class PhotoRestService {
	
	@Autowired
	private UserManager userManager = null; 
	
	@Autowired
	private PhotoManager photoManager = null;
    
	@RequestMapping(value = "/{photoId}/markbest", method = RequestMethod.GET)
	@ResponseBody
	public boolean markBest(@PathVariable String photoId) {
		
		Long id = null;
		
		User me = UserUtil.getCurrentUser(userManager);
		id = Long.parseLong(photoId);
		Photo photo = photoManager.getPhoto(id);
		if(photo.getOwner().equals(me)) {
			photoManager.markBest(id, true);
			return true;
		}else {
			return false;
		}
	}
	
	@RequestMapping(value = "/{photoId}/removebest", method = RequestMethod.GET)
	@ResponseBody
	public boolean removeBest(@PathVariable String photoId) {
		
		Long id = null;
		
		User me = UserUtil.getCurrentUser(userManager);
		id = Long.parseLong(photoId);
		Photo photo = photoManager.getPhoto(id);
		if(photo.getOwner().equals(me)) {
			photoManager.markBest(id, false);
			return true;
		}else {
			return false;
		}
	}
	
	@RequestMapping(value = "/{photoId}/properties", method = RequestMethod.POST)
	@ResponseBody
	public boolean properties(@PathVariable String photoId,
			@RequestBody final PhotoProperties properties) {
		
		Long id = null;
		
		User me = UserUtil.getCurrentUser(userManager);
		id = Long.parseLong(photoId);
		Photo photo = photoManager.getPhoto(id);
		if(photo.getOwner().equals(me)) {
			return photoManager.properties(id, properties);
		}else {
			return false;
		}
	}
	
	@RequestMapping(value = "/{photoId}/camerainfo", method = RequestMethod.GET)
	@ResponseBody
	public PhotoCameraInfo cameraInfo(@PathVariable String photoId) {
		
		Long id = null;
		id = Long.parseLong(photoId);
		return photoManager.getCameraInfo(id);
	}

}
