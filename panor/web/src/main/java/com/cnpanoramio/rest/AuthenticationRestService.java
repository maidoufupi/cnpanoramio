package com.cnpanoramio.rest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpanoramio.json.AuthResponse;
import com.cnpanoramio.json.AuthResponse.LoginRequest;
import com.cnpanoramio.json.AuthResponse.SignupCheckRes;
import com.cnpanoramio.service.UserSettingsManager;
import com.cnpanoramio.service.UserSettingsService;

@Controller
@RequestMapping("/api/rest/auth")
public class AuthenticationRestService extends AbstractRestService {

	private transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserSettingsManager userSettingsManager;
	@Autowired
	private UserSettingsService userSettingsService;
	
	@ResponseBody
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public AuthResponse login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
		AuthResponse response = new AuthResponse();

	    try {
	    	log.debug("username: " + loginRequest.getUsername() + " password: " + loginRequest.getPassword());
	    	
	        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
	        token.setDetails(new WebAuthenticationDetails(request));

	        Authentication auth = authenticationManager.authenticate(token);
	        SecurityContext securityContext = SecurityContextHolder.getContext();
	        securityContext.setAuthentication(auth);

	        if(auth.isAuthenticated()){
	            HttpSession session = request.getSession(true);
	            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	            User user = userSettingsManager.getUser(loginRequest.getUsername());
	            response.setUser(userSettingsService.getOpenInfo(user, user));
	            response.setStatus(AuthResponse.Status.OK.name());

	        }else{
	            SecurityContextHolder.getContext().setAuthentication(null);

	            response.setStatus(AuthResponse.Status.NO_AUTHORIZE.name());
	            log.debug("authenticate fail");
	        }   
	    } catch (Exception e) {    
	    	e.printStackTrace();
	    	response.setStatus(AuthResponse.Status.NO_AUTHORIZE.name()); 
	    	log.debug("authenticate exception");
	    }
	    return response;
	}
	
	@ResponseBody
	@RequestMapping(value="/login/check", method = RequestMethod.POST)
	public AuthResponse loginCheck(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
		AuthResponse response = new AuthResponse();
		response.setStatus(AuthResponse.Status.OK.name());
		log.debug("username: " + loginRequest.getUsername() + " password: " + loginRequest.getPassword());
		userSettingsService.loginCheck(loginRequest);
		return response;
	}
	
//	@ResponseBody
//	@RequestMapping(value="/logout", method = RequestMethod.POST)
//	public AuthResponse logout(HttpServletRequest request) {
//		AuthResponse response = new AuthResponse();
//		
//		try {
//			SecurityContextHolder.getContext().setAuthentication(null);
//			response.setStatus(AuthResponse.Status.OK.name());
//		}catch (Exception e) {    
//	    	e.printStackTrace();
//	    	response.setStatus(AuthResponse.Status.EXCEPTION.name()); 
//	    	log.debug("logout exception");
//	    }
//		return response;
//	}
	
	@ResponseBody
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public AuthResponse isLogin(HttpServletRequest request, HttpServletResponse httpResponse) {
		AuthResponse response = new AuthResponse();

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication auth = securityContext.getAuthentication();
        String name = auth.getName(); //get logged in username
        User user = userSettingsManager.getUser(name);
        response.setUser(userSettingsManager.getOpenInfo(user));
        response.setStatus(AuthResponse.Status.OK.name());

		return response;
	}
		
	@ResponseBody
	@RequestMapping(value="/signup/check/username", method = RequestMethod.GET)
	public SignupCheckRes signupCheckUsername(@RequestParam("value") String value,
			HttpServletRequest request, HttpServletResponse httpResponse) {
		SignupCheckRes response = new SignupCheckRes();
		response.setValue(value);
		
		if(StringUtils.hasLength(value)) {
			try {
				User user = userSettingsManager.getUser(value);
				response.setValid(false);
			}catch(UsernameNotFoundException ex) {
				response.setValid(true);
			}
		}else {
			response.setValid(false);
		}
		return response;
	}
	
//	@ResponseBody
//	@RequestMapping(value="/signup", method = RequestMethod.POST)
//	public AuthResponse signup(HttpServletRequest request, HttpServletResponse httpResponse) {
//		AuthResponse response = new AuthResponse();
//		
//		return response;
//	}
}
