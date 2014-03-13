package com.cnpanoramio.rest;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpanoramio.Constant;
import com.cnpanoramio.json.PhotoCameraInfo;
import com.cnpanoramio.service.IndexService;

@Controller
@RequestMapping("/api/rest/index")
public class IndexController {
	
	@Autowired
    private ServletContext sCtx;
	
	@Autowired
	private IndexService indexService;
	
	
	@RequestMapping(value = "/{photoIds}", method = RequestMethod.PUT)
	@ResponseBody
	public boolean setPhoto(@PathVariable String photoIds) {
		
		Collection<Long> photoIdsL = new ArrayList<Long>();
		
		String[] ids = photoIds.split(",");
		for(String id : ids) {
			photoIdsL.add(Long.parseLong(id));
		}
		return indexService.setIndexPhotos(photoIdsL);		
	}
	
	@RequestMapping(value = "/photo", method = RequestMethod.GET)
	@ResponseBody
	public Collection<PhotoCameraInfo> getPhoto() {
		return indexService.getIndexPhotos();		
	}
}
