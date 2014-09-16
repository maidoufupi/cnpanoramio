package com.cnpanoramio.dao;

import org.appfuse.dao.GenericDao;

import com.cnpanoramio.domain.UserSettings;

public interface UserSettingsDao extends GenericDao<UserSettings, Long> {

	/**
	 * 根据用户名获取用户设置
	 * 
	 * @param userName
	 * @return
	 */
	public UserSettings getByUserName(String userName);
		
}
