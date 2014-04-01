package com.cnpanoramio.service;

import java.io.InputStream;

import org.appfuse.model.User;
import org.springframework.web.multipart.MultipartFile;

import com.cnpanoramio.domain.Avatar;
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.json.UserOpenInfo;

public interface UserSettingsManager {
	
	public static String AVATAR_FILE_TYPE = "png";
	
	/**
	 * 获取当前用户的详细设置
	 * 
	 * @return
	 */
	public UserSettings getCurrentUserSettings();
	
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
	 * 保存用户详细设置
	 * 
	 * @param settings
	 * @return
	 */
	public UserSettings save(UserSettings settings);
	
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
