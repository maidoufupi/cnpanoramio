package com.cnpanoramio.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.json.PhotoCameraInfo;
import com.cnpanoramio.json.PhotoProperties;
import com.cnpanoramio.service.FileService;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.utils.UserUtil;

@Controller
@RequestMapping("/api/rest/photo")
public class PhotoRestService {
	
	@Autowired
	private UserManager userManager = null; 
	
	@Autowired
	private PhotoManager photoService;
	
	@Autowired
	private FileService fileService;
    
	@RequestMapping(value = "/{photoId}/best", method = RequestMethod.PUT)
	@ResponseBody
	public boolean markBest(@PathVariable String photoId) {
		
		Long id = null;
		
		User me = UserUtil.getCurrentUser(userManager);
		id = Long.parseLong(photoId);
		Photo photo = photoService.getPhoto(id);
		if(photo.getOwner().equals(me)) {
			photoService.markBest(id, true);
			return true;
		}else {
			return false;
		}
	}
	
	@RequestMapping(value = "/{photoId}/best", method = RequestMethod.DELETE)
	@ResponseBody
	public boolean removeBest(@PathVariable String photoId) {
		
		Long id = null;
		
		User me = UserUtil.getCurrentUser(userManager);
		id = Long.parseLong(photoId);
		Photo photo = photoService.getPhoto(id);
		if(photo.getOwner().equals(me)) {
			photoService.markBest(id, false);
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
		Photo photo = photoService.getPhoto(id);
		if(photo.getOwner().equals(me)) {
			return photoService.properties(id, properties);
		}else {
			return false;
		}
	}
	
	@RequestMapping(value = "/{photoId}/camerainfo", method = RequestMethod.GET)
	@ResponseBody
	public PhotoCameraInfo cameraInfo(@PathVariable String photoId) {
		
		Long id = null;
		id = Long.parseLong(photoId);
		return photoService.getCameraInfo(id);
	}
	
	@RequestMapping(value = "/{photoId}/{level}", 
			method = RequestMethod.GET,
			produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	@ResponseBody
	public FileSystemResource getPhoto(@PathVariable String photoId, @PathVariable int level) {
		
		Long id = null;
		id = Long.parseLong(photoId);
//		Photo photo = photoService.getPhoto(id);
		
		File file = fileService.readFile(FileService.TYPE_IMAGE, id, level);

		return new FileSystemResource(file);
	}

	@RequestMapping(value = "/{photoId}", method = RequestMethod.DELETE)
	@ResponseBody
	public PhotoProperties delete(@PathVariable String photoId) {
		Long id = null;
		id = Long.parseLong(photoId);
		return photoService.delete(id);
	}
	
	@RequestMapping(value = "/{photoId}", method = RequestMethod.GET)
	@ResponseBody
	public PhotoProperties get(@PathVariable String photoId) {
		Long id = null;
		id = Long.parseLong(photoId);
		return photoService.getPhotoProperties(id);
	}
}
