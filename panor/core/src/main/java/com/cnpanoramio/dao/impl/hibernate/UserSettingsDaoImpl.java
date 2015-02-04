package com.cnpanoramio.dao.impl.hibernate;

import java.util.List;
import java.util.Set;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.appfuse.model.User;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.dao.UserSettingsDao;
import com.cnpanoramio.domain.Like;
import com.cnpanoramio.domain.Tag;
import com.cnpanoramio.domain.UserSettings;

@Repository("userSettingsDao")
public class UserSettingsDaoImpl extends GenericDaoHibernate<UserSettings, Long> implements UserSettingsDao {

	public UserSettingsDaoImpl() {
		super(UserSettings.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public UserSettings getByUserName(String userName) {
		Query photoListQuery = getSession().createQuery("select us from UserSettings as us left join us.user as u where u.username = :username");
		
		photoListQuery.setParameter("username", userName);
		return (UserSettings) photoListQuery.uniqueResult();
	}

	@Override
	public void addSaveUser(User user) {
		// TODO Auto-generated method stub
		String sql="UPDATE app_user SET email='"
			+user.getEmail()+"',first_name='"+user.getFirstName()
			+"',password='"+user.getPassword()+"',username='"+user.getFirstName()+"',Website='"+user.getWebsite()+"',credentials_expired="+user.isAccountExpired() 	
			+" WHERE id="+user.getId();
		
		Query query = getSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	@Override
	public UserSettings getByUserOpenId(String openId) {
		Query photoListQuery = getSession().createQuery("select us from UserSettings as us left join us.user as u where u.website = :website");	
		photoListQuery.setParameter("website", openId);
		return (UserSettings) photoListQuery.uniqueResult();
	}

	@Override
	public void updateusersetting(User user) {
		String sql="UPDATE user_settings SET name='"+user.getFirstName()+"'WHERE id="+user.getId();
		Query query = getSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	@Override
	public UserSettings getByUserWeiBo(String weibo) {
		Query photoListQuery = getSession().createQuery("select us from UserSettings as us left join us.user as u where u.website = :website");		
		photoListQuery.setParameter("website", weibo);
		return (UserSettings) photoListQuery.uniqueResult();
	}

//	@Override
//	public List<String> getUserTags(User user) {
//		Query query = getSession()
//				.createQuery("select distinct t.tag from Photo as p inner join p.tags as t where p.owner = :user");
//		query.setParameter("user", user);
//		List res = query.list();
//		return res;
//	}
//
//	@Override
//	public Tag getTag(String tag) {
//		Query query = getSession()
//				.createQuery("from Tag where tag = :tag");
//		query.setParameter("tag", tag);
//		return (Tag) query.uniqueResult();
//	}
//
//	@Override
//	public Set<Tag> createTag(UserSettings user, String tag) {
//		Tag t = getTag(tag);
//		if(null == t) {
//			t = new Tag(tag);
//		}
//		user.getTags().add(t);
//		return user.getTags();
//	}

//	@Override
//	public Tag getOrCreateUserTag(UserSettings user, String tag) {
//		
//		Tag tagObj = (Tag)getSession().createCriteria(Tag.class)
//				.add(Restrictions.eq("user", user))
//				.add(Restrictions.eq("tag", tag)).uniqueResult();
//		if(null == tagObj) {
//			tagObj = new Tag(tag);
//			tagObj.setUser(user);
//			user.getTags().add(tagObj);
//		}
//		return tagObj;
//	}

//	@Override
//	public Set<User> getUserFollowers(User user) {
//		UserSettings settings = (UserSettings) getSession().createCriteria(UserSettings.class)
//				.add(Restrictions.eq("id", user.getId())).uniqueResult();
//		
//		return settings.getFollower();
//	}
}
