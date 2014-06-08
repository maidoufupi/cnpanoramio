package com.cnpanoramio.spect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Aspect
public class UserSecurityAspect {

	private transient final Log log = LogFactory.getLog(getClass());
	
	/**
	 * 需要处理自己的数据时用到的权限检查
	 * 
	 * @param joinPoint
	 */
	@Before("execution(public * com.cnpanoramio.service.impl.*.*My*(..))")
	public void processMyTravelAdvice(JoinPoint joinPoint) {
		SecurityContext ctx = SecurityContextHolder.getContext();
		if (ctx.getAuthentication() != null) {
			Authentication auth = ctx.getAuthentication();
			
			User user = (User) joinPoint.getArgs()[0];
			User currentUser = getCurrentUser(auth);
			if(!user.getId().equals(currentUser.getId())) {
				throw new AccessDeniedException("Access Denied! User Id: " + user.getId());
			}else {
				log.debug("Access Passed! User Id: " + user.getId());
				throw new AccessDeniedException("Access Passed! User Id: " + user.getId());
			}
		}
	}
	
	private User getCurrentUser(Authentication auth) {
        User currentUser;
        if (auth.getPrincipal() instanceof UserDetails) {
            currentUser = (User) auth.getPrincipal();
        } else if (auth.getDetails() instanceof UserDetails) {
            currentUser = (User) auth.getDetails();
        } else {
            throw new AccessDeniedException("User not properly authenticated.");
        }
        return currentUser;
    }
	
}
