package com.cnpanoramio.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.json.PhotoProperties;
import com.cnpanoramio.json.UserTransfer;
import com.cnpanoramio.service.PhotoManager;

@Controller
@RequestMapping("/api/rest/user")
public class UserRestService {
	
	@Autowired
	private UserManager userManager = null; 
	
	@Autowired
	private PhotoManager photoService;
	
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authManager;
	
	@RequestMapping(value = "/{userId}/photos/{pageSize}/{pageNo}", method = RequestMethod.GET)
	@ResponseBody
	public Collection<PhotoProperties> getPhotos(
			@PathVariable String userId, 
			@PathVariable String pageSize,
			@PathVariable String pageNo) {
		
		int pageSizeI,
		    pageNoI;
		pageSizeI = Integer.valueOf(pageSize).intValue();
		pageNoI = Integer.valueOf(pageNo).intValue();
		
//		User me = UserUtil.getCurrentUser(userManager);
		
		Collection<PhotoProperties> photos = new ArrayList<PhotoProperties>();
		Collection<Photo> ps = photoService.getPhotosForUser(userId, pageSizeI, pageNoI);
		for (Photo photo : ps) {
			PhotoProperties pp = new PhotoProperties();
			pp.setId(photo.getId());
			pp.setTitle(photo.getTitle());
			pp.setDescription(photo.getDescription());
			photos.add(pp);
		}
		
		return photos;
	}
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	@ResponseBody
	public UserTransfer authenticate(@RequestBody User user) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				user.getUsername(), user.getPassword());
		try {
			Authentication authentication = this.authManager
					.authenticate(authenticationToken);
			SecurityContextHolder.getContext()
					.setAuthentication(authentication);
			Map<String, Boolean> roles = new HashMap<String, Boolean>();
			for (GrantedAuthority authority : authentication.getAuthorities()) {
				roles.put(authority.toString(), Boolean.TRUE);
			}
			
			User userObject = userManager.getUserByUsername(user.getUsername());

			return new UserTransfer(userObject.getId(), userObject.getUsername(), true, roles, "");

		} catch (AuthenticationException e) {
			return new UserTransfer(0L, user.getUsername(), false, null, "");
		}
	}

}
