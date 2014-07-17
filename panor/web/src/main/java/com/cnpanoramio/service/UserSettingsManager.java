package com.cnpanoramio.service;

import java.io.InputStream;
import java.util.List;

import org.appfuse.model.User;

import com.cnpanoramio.domain.Avatar;
import com.cnpanoramio.domain.Recycle;
import com.cnpanoramio.domain.Tag;
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
	 * 获取用户设置
	 * 
	 * @param id
	 * @return
	 */
	public UserSettings get(Long id);
	
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
	
	/**
	 * 获取用户所有的tag
	 * 
	 * @param user
	 * @return
	 */
	public List<String> getUserTags(User user);
	
	/**
	 * 创建用户的tag
	 * 
	 * @param user
	 * @param tag
	 * @return
	 */
	public List<String> createTag(User user, String tag);
	
	/**
	 * 删除用户tag
	 * 
	 * @param user
	 * @param tag
	 * @return
	 */
	public List<String> deleteTag(User user, String tag);
	
	/**
	 * 获取用户的回收站全部内容
	 * 
	 * @param id 用户id
	 */
	public List<Recycle> getRecycleBin(Long id);
	
	/**
	 * 清空用户的回收站
	 * 
	 * @param id 用户id
	 */
	public void emptyRecycleBin(Long id);
	
	/**
	 * 撤销删除操作
	 * 
	 * @param id 删除记录的id
	 */
	public void cancelRecycle(Long userId, Long id);
	
	/**
	 * 永久删除操作
	 * 
	 * @param id 删除记录的id
	 */
	public void removeRecycle(Long userId, Long id);
}
