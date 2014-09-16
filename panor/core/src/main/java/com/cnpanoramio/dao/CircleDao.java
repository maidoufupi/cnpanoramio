package com.cnpanoramio.dao;

import java.util.List;

import org.appfuse.dao.GenericDao;
import org.appfuse.model.User;

import com.cnpanoramio.domain.Circle;


public interface CircleDao extends GenericDao<Circle, Long> {

	public Circle persist(Circle circle);
	
	/**
	 * 获取用户的圈子
	 * 
	 * @param user
	 * @return
	 */
	public List<Circle> getUserCircles(User user);
	
	/**
	 * 获取用户指定名称的圈子
	 * 
	 * @param user
	 * @param name
	 * @return
	 */
	public Circle getUserCircleByName(User user, String name);
	
	/**
	 * 推荐关注
	 * 
	 * @param user
	 * @return
	 */
	public List<User> getFollowSuggested(User user, int pageSize, int pageNo);
}
