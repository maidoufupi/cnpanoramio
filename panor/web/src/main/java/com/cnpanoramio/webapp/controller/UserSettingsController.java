package com.cnpanoramio.webapp.controller;

import org.apache.commons.lang.StringUtils;
import org.appfuse.Constants;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserExistsException;
import org.appfuse.service.UserManager;

import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.service.UserSettingsManager;
import com.cnpanoramio.webapp.util.RequestUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Locale;

/**
 * Implementation of <strong>SimpleFormController</strong> that interacts with
 * the {@link UserManager} to retrieve/persist values to the database.
 *
 * <p><a href="UserFormController.java.html"><i>View Source</i></a>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/userSettings*")
public class UserSettingsController extends BaseFormController {
	
	@Autowired
	private UserSettingsManager userSettingsService = null;
	
    @RequestMapping(method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response) {
    	UserSettings us = userSettingsService.getCurrentUserSettings();
    	request.setAttribute("settings", us);
    	return new ModelAndView("userSettings");
    }

	public UserSettingsManager getUserSettingsService() {
		return userSettingsService;
	}

	public void setUserSettingsService(UserSettingsManager userSettingsService) {
		this.userSettingsService = userSettingsService;
	}    
}
