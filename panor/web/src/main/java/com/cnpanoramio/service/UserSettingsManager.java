package com.cnpanoramio.service;

import org.appfuse.model.User;

import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.json.UserOpenInfo;

public interface UserSettingsManager {
	
	public UserSettings getCurrentUserSettings();
	public UserSettings getSettingsByUserName(String userName);
	
	/**
	 * 获取用户的公开信息
	 * 
	 * @param id
	 * @return
	 */
	public UserOpenInfo getOpenInfo(Long id);
	
	/**
	 * 根据email查询用户
	 * 
	 * @param nameOrEmail 用户名或email
	 * @return
	 */
	public User getUser(String nameOrEmail);
	
}
