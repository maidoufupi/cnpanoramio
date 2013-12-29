package com.cnpanoramio.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoDetails;
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.UserSettingsManager;

@Controller
@RequestMapping("/photo")
public class PhotoController extends BaseFormController {

	private UserManager userManager = null;
	private PhotoManager photoService = null;
	
	@Autowired
	private UserSettingsManager userSettingsService = null;

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
		String username = null;
		HttpSession httpSession = request.getSession();
		if(null != httpSession) {
			Object nameObject = httpSession.getAttribute("username");
			if(null != nameObject) {
				username = nameObject.toString() ;
			}
			
			if(null != username) {
				UserSettings userSettings = userSettingsService.getSettingsByUserName(username);
				if(null != userSettings) {
					request.setAttribute("userSettings", userSettings); 
				}
			}			
		}

		Photo photo = photoService.getPhoto(photoId);
		PhotoDetails details = photo.getDetails();
		
		request.setAttribute("photo", photo);
		request.setAttribute("details", details);
		
		return getSuccessView();
	}
}
