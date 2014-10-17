package com.cnpanoramio.service;

import org.appfuse.model.User;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.json.AuthResponse.LoginRequest;
import com.cnpanoramio.json.UserOpenInfo;
import com.cnpanoramio.json.UserSettings;
import com.cnpanoramio.json.UserSettingsResponse;


public interface UserSettingsService {
	
	public void save(UserSettings settings);
	
	/**
	 * 获取用户信息
	 * 
	 * @param user
	 * @return
	 */
	public UserOpenInfo getOpenInfo(User user, User me);
		
	/**
	 * 获取用户的详细设置
	 * 
	 * @param user
	 * @return
	 */
	public UserSettings getUserSettings(User user);
	
	/**
	 * 更改密码
	 * 
	 * @param user
	 * @param password
	 */
	public void changePassword(User user, UserSettingsResponse.Password password);
	
	/**
	 * 更改地图供应商
	 * 
	 * @param mapVendor
	 * @return
	 */
	public UserSettings changeMapVendor(User user, MapVendor mapVendor);
	
	/**
	 * 更改用户账号设置
	 * 
	 * @param user
	 * @param settings
	 * @return
	 */
	public UserSettings changeAccount(User user, UserSettings settings);
	
	/**
	 * 更改上传设置
	 * 
	 * @param user
	 * @param settings
	 * @return
	 */
	public UserSettings changeUpload(User user, UserSettings settings);
	
	/**
	 * 验证登录用户
	 * 
	 * @param loginRequest
	 * @return
	 */
	public boolean loginCheck(LoginRequest loginRequest);
}
