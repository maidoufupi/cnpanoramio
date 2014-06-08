package com.cnpanoramio.webapp.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cnpanoramio.service.IndexService;

/**
 * Implementation of <strong>SimpleFormController</strong> that interacts with
 * the {@link UserManager} to retrieve/persist values to the database.
 *
 * <p><a href="UserFormController.java.html"><i>View Source</i></a>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/")
public class IndexPageController extends BaseFormController {
	
	@Autowired
	private IndexService indexService = null;
	
	@Autowired
    private ServletContext sCtx;
	
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response) {
    	
//    	Collection<PhotoCameraInfo> photos = indexService.getIndexPhotos();
//    	
//    	request.setAttribute("photos", photos);
    	
    	return new ModelAndView("map");
    }
  
}
