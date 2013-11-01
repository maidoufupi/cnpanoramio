package com.cnpanoramio.webapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cnpanoramio.service.PhotoManager;

@Controller
@RequestMapping("/photo")
public class PhotoController extends BaseFormController {

	private UserManager userManager = null;
	private PhotoManager photoService = null;

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
    	
	@Autowired
	public void setPhotoService(PhotoManager photoService) {
		this.photoService = photoService;
	}
    
	public PhotoController() {
		setCancelView("redirect:/mainMenu");
		setSuccessView("photoDisplay");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{photoId}")
	public String showPhoto(@PathVariable("photoId") Long photoId,
			HttpServletRequest request) {
		
//		Object principal = SecurityContextHolder.getContext()
//				.getAuthentication().getPrincipal();
//		String username;
//		if (principal instanceof UserDetails) {
//			username = ((UserDetails) principal).getUsername();
//		} else {
//			username = principal.toString();
//		}
//		User user = userManager.getUserByUsername(username);
//		
//		Photo photo = photoService.getPhoto(photoId);
		
		request.setAttribute("photoId", photoId);
		
		return getSuccessView();
	}
}
