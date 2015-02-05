package com.cnpanoramio.dao;

import org.appfuse.dao.GenericDao;
import org.appfuse.model.User;

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
	 * 更新腾讯用户信息
	 * @param user
	 */
	public void addSaveUser(User user);
	/**
	 * 查询腾讯用户openId
	 * @param openId
	 * @return
	 */
	public UserSettings getByUserOpenId(String openId);
	/**
	 * 更新腾讯转来的用户表
	 * @param user
	 */
	public void updateusersetting(User user);
	/**
	 * 查询微博用户Id
	 * @param weibo
	 * @return
	 */
	public UserSettings getByUserWeiBo(String weibo);
}
