package com.cnpanoramio.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.CircleDao;
import com.cnpanoramio.dao.UserSettingsDao;
import com.cnpanoramio.domain.Circle;
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.json.UserOpenInfo;
import com.cnpanoramio.service.CircleManager;
import com.cnpanoramio.service.UserSettingsManager;
import com.cnpanoramio.service.UserSettingsService;
import com.cnpanoramio.utils.UserUtil;

@Service
@Transactional
public class CircleManagerImpl implements CircleManager {

	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private CircleDao circleDao;
	
	@Autowired
	private UserSettingsService userSettingsService;
	
	@Autowired
	private UserSettingsManager userSettingsManager;
	
	@Autowired
	private UserSettingsDao userSettingsDao;
	
	@Autowired
	private UserManager userManager;

	@Override
	public List<com.cnpanoramio.json.UserResponse.Circle> getCircles(User user) {
		List<Circle> circles = circleDao.getUserCircles(user);
		List<com.cnpanoramio.json.UserResponse.Circle> ccs = new ArrayList<com.cnpanoramio.json.UserResponse.Circle>(0);
		for(Circle circle : circles) {
			ccs.add(transformCircle(circle));
		}
			
		return ccs;
	}

	@Override
	public com.cnpanoramio.json.UserResponse.Circle getCircle(User user, Long id) {
		Circle circle = circleDao.get(id);
		if(circle.getOwner().equals(user)) {
			return transformCircle(circle);
		}else {
			throw new AccessDeniedException("用户 " + user.getId() + " 未授权, circle.id = " + id);
		}
	}

	@Override
	public com.cnpanoramio.json.UserResponse.Circle getCircleByName(User user,
			String name) {
		Circle circle = circleDao.getUserCircleByName(user, name);
		if(circle.getOwner().equals(user)) {
			return transformCircle(circle);
		}else {
			throw new AccessDeniedException("用户 " + user.getId() + " 未授权, circle.id = " + name);
		}
	}
	
	private com.cnpanoramio.json.UserResponse.Circle transformCircle(Circle circle) {
		com.cnpanoramio.json.UserResponse.Circle cc = new com.cnpanoramio.json.UserResponse.Circle();
		cc.setId(circle.getId());
		cc.setName(circle.getName());
		for(User member : circle.getUsers()) {
			cc.getUsers().add(UserUtil.getSimpleOpenInfo(userSettingsManager.get(member.getId())));
		}
		return cc;
	}

	@Override
	public List<UserOpenInfo> getUserFollowers(User user) {
		UserSettings settings = userSettingsManager.get(user);
		Set<Circle> circles = settings.getCircles();
		Set<User> members = settings.getFollower();
		List<UserOpenInfo> followers = new ArrayList<UserOpenInfo>(0);
		
		for(User member : members) {
			UserOpenInfo uInfo = UserUtil.getSimpleOpenInfo(userSettingsManager.get(member.getId()));
			for(Circle circle : circles) {
				for(User u : circle.getUsers()) {
					if(u.equals(member)) {
						uInfo.setFollow(true);
						break;
					}
				}
				if(null != uInfo.getFollow() && uInfo.getFollow()) {
					break;
				}
			}
			followers.add(uInfo);
		}
		return followers;
	}
	
	public boolean isFollow(User owner, User user) {
//		User owner = UserUtil.getCurrentUser(userManager);
		UserSettings settings = userSettingsManager.get(owner);
		Set<Circle> circles = settings.getCircles();
		for(Circle circle : circles) {
			for(User u : circle.getUsers()) {
				if(u.equals(user)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void deleteUserFollower(User user, Long followerId) {
		UserSettings settings = userSettingsDao.get(user.getId());
		for(User member : settings.getFollower()) {
			if(member.getId().equals(followerId)) {
				List<Circle> circles = circleDao.getUserCircles(member);
				for(Circle circle : circles) {
					circle.getUsers().remove(user);
				}
				settings.getFollower().remove(member);
			}
		}
	}

	@Override
	public List<UserOpenInfo> getFollowSuggested(User user, int pageSize, int pageNo) {
		List<User> users = circleDao.getFollowSuggested(user, pageSize, pageNo);
		List<UserOpenInfo> userInfos = new ArrayList<UserOpenInfo>();
		for(User u : users) {
			userInfos.add(userSettingsService.getOpenInfo(u, user));
		}
		return userInfos;
	}
}
