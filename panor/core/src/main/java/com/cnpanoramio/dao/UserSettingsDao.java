package com.cnpanoramio.dao;

import java.util.List;
import java.util.Set;

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
	
	/**
	 * 根据文本获取tag对象
	 * 
	 * @param tag
	 * @return
	 */
	public Tag getTag(String tag);
	
	/**
	 * 为用户创建tag
	 * 
	 * @param user
	 * @param tag
	 * @return
	 */
	public Set<Tag> createTag(UserSettings user, String tag);
	
	/**
	 * 获取或创建用户的标签
	 * 
	 * @return
	 */
	public Tag getOrCreateUserTag(UserSettings user, String tag);
	
}
