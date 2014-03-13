package com.cnpanoramio.utils;

import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class UserUtil {

	public synchronized static User getCurrentUser(UserManager mgr) {
		Object obj = SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String username;
		if (obj instanceof UserDetails) {
			username = ((UserDetails) obj).getUsername();
		} else {
			username = obj.toString();
		}

		return username != null ? mgr.getUserByUsername(username) : null;
	}
}
