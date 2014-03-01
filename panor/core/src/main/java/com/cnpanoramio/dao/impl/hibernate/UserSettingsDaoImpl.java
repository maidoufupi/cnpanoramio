package com.cnpanoramio.dao.impl.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.UserSettingsDao;
import com.cnpanoramio.domain.UserSettings;

@Repository("userSettingsDao")
public class UserSettingsDaoImpl extends GenericDaoHibernate<UserSettings, Long> implements UserSettingsDao {

	public UserSettingsDaoImpl() {
		super(UserSettings.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public UserSettings getByUserName(String userName) {
		Query photoListQuery = getSession().createQuery("select us from UserSettings as us left join us.user as u where u.username = :username");
		
		photoListQuery.setParameter("username", userName);
		List res = photoListQuery.list();
        if(res.size() > 0) {
        	return (UserSettings) res.get(0);
        }else {
        	return null;
        }
	}
}
