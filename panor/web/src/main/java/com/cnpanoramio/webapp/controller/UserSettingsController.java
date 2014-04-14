package com.cnpanoramio.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.service.UserSettingsManager;

/**
 * Implementation of <strong>SimpleFormController</strong> that interacts with
 * the {@link UserManager} to retrieve/persist values to the database.
 *
 * <p><a href="UserFormController.java.html"><i>View Source</i></a>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/settings*")
public class UserSettingsController extends BaseFormController {
	
	@Autowired
	private UserSettingsManager userSettingsService = null;
	
    @RequestMapping(method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response) {
//    	UserSettings us = userSettingsService.getCurrentUserSettings();
//    	request.setAttribute("settings", us);
    	return new ModelAndView("settings");
    }

	public UserSettingsManager getUserSettingsService() {
		return userSettingsService;
	}

	public void setUserSettingsService(UserSettingsManager userSettingsService) {
		this.userSettingsService = userSettingsService;
	}    
}
