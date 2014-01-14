package com.cnpanoramio.webapp.rest;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpanoramio.Constant;

@Controller
@RequestMapping("/api/rest/index")
public class IndexController {
	
	@Autowired
    private ServletContext sCtx;
	
	@RequestMapping(value = "/{photoId}", method = RequestMethod.GET)
	@ResponseBody
	public boolean getMovie(@PathVariable String photoId) {
		try {
			sCtx.setAttribute(Constant.INDEX_PHOTO_ID, Long.parseLong(photoId));
		} catch (Exception e) {
			return false;
		}			
		return true;
	}

}
