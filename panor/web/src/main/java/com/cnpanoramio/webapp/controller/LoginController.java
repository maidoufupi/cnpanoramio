package com.cnpanoramio.webapp.controller;

import com.cnpanoramio.service.UserSettingsManager;
import org.appfuse.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by tiwen.wang on 11/20/2014.
 */
@Controller
public class LoginController extends BaseFormController {

    @Autowired
    private UserSettingsManager userSettingsManager;

    public LoginController() {
        setSuccessView("redirect:/");
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String handleRequest(HttpServletRequest request,
                                HttpServletResponse response) {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication  = securityContext.getAuthentication();
        if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
            return getSuccessView();
        }else {
            return "login";
        }
    }
}
