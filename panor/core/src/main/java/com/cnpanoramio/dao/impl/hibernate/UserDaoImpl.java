package com.cnpanoramio.dao.impl.hibernate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.appfuse.dao.hibernate.UserDaoHibernate;
import org.appfuse.model.User;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository("userloginDao")
public class UserDaoImpl extends UserDaoHibernate {

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher m = pattern.matcher(username);  
        boolean email = m.matches();  
        if(email) {
        	List users = getSession().createCriteria(User.class).add(Restrictions.eq("email", username)).list();
            if (users == null || users.isEmpty()) {
                throw new UsernameNotFoundException("user '" + username + "' not found...");
            } else {
                return (UserDetails) users.get(0);
            }
        } else {  
        	return super.loadUserByUsername(username);
        }  
		
	}

	
}
