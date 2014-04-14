package com.cnpanoramio.webapp.controller;

import org.appfuse.dao.SearchException;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.UserSettingsManager;

/**
 * Simple class to retrieve a list of users from the database.
 * <p/>
 * <p>
 * <a href="UserController.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	public static int pageSize = 10;
	
	private UserManager userManager = null;
	
	@Autowired
	private UserSettingsManager userSettingsManager = null;
	@Autowired
	private PhotoManager photoManager = null;

	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public ModelAndView getUserPage(@PathVariable String userId)
			throws Exception {
		Model model = new ExtendedModelMap();
				
		try {
			User user = userManager.get(Long.parseLong(userId));
		} catch(ObjectRetrievalFailureException ex) {
			return new ModelAndView("redirect:/404.jsp");
		}catch (Exception se) {			
			return new ModelAndView("redirect:/404.jsp");
		}
		return new ModelAndView("user", model.asMap());
	}
	
	@RequestMapping(value = "/{userId}/tags/{tag}", method = RequestMethod.GET)
	public ModelAndView getUserPageTags(
			@PathVariable String userId, 
			@PathVariable String tag)
			throws Exception {
		Model model = new ExtendedModelMap();
		try {
			User user = userManager.get(Long.parseLong(userId));
			UserSettings userSettings = userSettingsManager.getSettingsByUserName(user.getUsername());
			model.addAttribute("user", user);
			model.addAttribute("userSettings", userSettings);
		} catch(ObjectRetrievalFailureException ex) {
			
		}
		catch (SearchException se) {
			model.addAttribute("searchError", se.getMessage());
		}
		return new ModelAndView("user", model.asMap());
	}
}
