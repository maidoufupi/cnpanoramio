package com.cnpanoramio.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoDetails;
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.json.PhotoResponse;
import com.cnpanoramio.service.CommentService;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.UserSettingsManager;
import com.cnpanoramio.service.ViewsManager;
import com.cnpanoramio.utils.UserUtil;

@Controller
@RequestMapping("/map_photo")
public class MapPhotoController extends BaseFormController {

	@Autowired
	private UserManager userManager = null;

	public MapPhotoController() {
		setCancelView("redirect:/mainMenu");
		setSuccessView("map_photo");
	}

	@RequestMapping(method = RequestMethod.GET)
	public String mapPhoto(HttpServletRequest request) {
		User me;
		try {
			me = UserUtil.getCurrentUser(userManager);
		} catch (UsernameNotFoundException ex) {
			return "redirect:/login";
		}
		
		return getSuccessView();
	}
}
