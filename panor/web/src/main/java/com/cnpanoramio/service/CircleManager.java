package com.cnpanoramio.service;

import java.util.List;

import org.appfuse.model.User;

import com.cnpanoramio.json.UserOpenInfo;

public interface CircleManager {

	/**
	 * 获取用户的圈子
	 * 
	 * @param user
	 * @return
	 */
	public List<com.cnpanoramio.json.UserResponse.Circle> getCircles(User user);
	
	/**
	 * 
	 * @param user
	 * @param id
	 * @return
	 */
	public com.cnpanoramio.json.UserResponse.Circle getCircle(User user, Long id);
	
	/**
	 * 
	 * @param user
	 * @param name
	 * @return
	 */
	public com.cnpanoramio.json.UserResponse.Circle getCircleByName(User user, String name);
	
	/**
	 * 获取用户的Followers
	 * 
	 * @param user
	 * @return
	 */
	public List<com.cnpanoramio.json.UserOpenInfo> getUserFollowers(User user);
	
	/**
	 * 拉黑
	 * 
	 * @param user
	 */
	public void deleteUserFollower(User user, Long followerId);
	
	/**
	 * 判断用户owner是否follow了用户user
	 * 
	 * @param owner
	 * @param user
	 * @return
	 */
	public boolean isFollow(User owner, User user);
	
	/**
	 * 为用户推荐关注
	 * 
	 * @param user
	 * @return
	 */
	public List<UserOpenInfo> getFollowSuggested(User user, int pageSize, int pageNo);
	
}
