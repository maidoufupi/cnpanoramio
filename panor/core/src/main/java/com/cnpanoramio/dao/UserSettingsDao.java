package com.cnpanoramio.dao;

import java.util.List;

import org.appfuse.dao.GenericDao;
import org.appfuse.model.User;

import com.cnpanoramio.domain.Tag;
import com.cnpanoramio.domain.UserSettings;

public interface UserSettingsDao extends GenericDao<UserSettings, Long> {

	/**
	 * 根据用户名获取用户设置
	 * 
	 * @param userName
	 * @return
	 */
	public UserSettings getByUserName(String userName);
	
	/**
	 * 获取用户所有的tag
	 * 
	 * @param user
	 * @return
	 */
	public List<String> getUserTags(User user);
}
