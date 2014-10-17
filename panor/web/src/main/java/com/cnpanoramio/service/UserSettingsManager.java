package com.cnpanoramio.service;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.appfuse.model.User;

import com.cnpanoramio.domain.Avatar;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.Tag;
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.json.PhotoProperties;
import com.cnpanoramio.json.UserOpenInfo;

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
	 * 获取用户设置
	 * 
	 * @param id
	 * @return
	 */
	public UserSettings get(Long id);
	
	public UserSettings get(User user);
	
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
	public UserOpenInfo getOpenInfo(User user, User owner);
	
	public UserOpenInfo getOpenInfo(User user);
	
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
	public Set<String> getUserTags(User user);
	
	/**
	 * 创建用户的tag
	 * 
	 * @param user
	 * @param tag
	 * @return
	 */
	public Tag createTag(User user, String tag);
	
	/**
	 * 删除用户tag
	 * 
	 * @param user
	 * @param tag
	 * @return
	 */
	public Set<String> deleteTag(User user, String tag);
	
	/**
	 * 获取用户的回收站全部内容
	 * 
	 * @param id 用户id
	 */
//	public List<Recycle> getRecycleBin(Long id);
	
	
	
	
	/**
	 * 获取用户指定区域内的全部图片
	 * 
	 * @param id
	 * @param pageSize
	 * @param pageNo
	 * @param swLat
	 * @param swLng
	 * @param neLat
	 * @param neLng
	 * @return
	 */
	public List<PhotoProperties> getPhotosForUserBounds(String id, int pageSize, int pageNo,
			Double swLat, Double swLng, Double neLat, Double neLng);
	
	/**
	 * 注册新用户
	 * 
	 * @param user
	 * @return
	 */
//	public User signup(User user);
	
	/**
	 * 关注/取消关注 用户
	 * 
	 * @param userId
	 * @param followingId
	 * @return
	 */
	public void following(User user, User following, boolean follow);
	
	/**
	 * 用户密码是否正确
	 * 
	 * @param user
	 * @param password
	 * @return
	 */
	public boolean isPasswordValid(User user, String password);
	
	/**
	 * 增加存储空间占用
	 * 
	 * @param photo
	 */
	public void addStorageSpace(User user, Photo photo);
	
	/**
	 * 减少存储空间占用
	 * 
	 * @param user
	 * @param photo
	 */
	public void removeStorageSpace(User user, Photo photo);
}
