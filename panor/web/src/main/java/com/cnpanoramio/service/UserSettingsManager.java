package com.cnpanoramio.service;

import java.io.InputStream;

import org.appfuse.model.User;
import org.springframework.web.multipart.MultipartFile;

import com.cnpanoramio.domain.Avatar;
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.json.UserOpenInfo;
import com.cnpanoramio.json.UserResponse.Settings;

public interface UserSettingsManager {
	
	public static String AVATAR_FILE_TYPE = "png";
	
	/**
	 * 保存用户详细设置
	 * 
	 * @param userSettings
	 * @return
	 */
	public UserSettings save(UserSettings userSettings);
	
	/**
	 * 创建用户详细设置
	 * 
	 * @param user
	 * @return
	 */
	public UserSettings create(User user);
	
	/**
	 * 获取当前用户的详细设置
	 * 
	 * @return
	 */
	public Settings getCurrentUserSettings();
	
	/**
	 * 通过用户名查找详细设置
	 * 
	 * @param userName
	 * @return
	 */
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
		
	/**
	 * 
	 * @return
	 */
	public Avatar saveAvatar(User user, InputStream ins);
	
	/**
	 * 
	 * @return
	 */
	public Avatar getAvatar(Long id);
	
	/**
	 * 
	 * @return
	 */
	public Avatar getUserAvatar(Long userId);
	
}
