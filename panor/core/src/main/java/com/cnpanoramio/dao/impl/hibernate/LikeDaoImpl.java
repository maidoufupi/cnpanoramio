package com.cnpanoramio.dao.impl.hibernate;

import java.util.Calendar;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.appfuse.model.User;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.dao.LikeDao;
import com.cnpanoramio.domain.Comment;
import com.cnpanoramio.domain.Like;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.Travel;

@Repository("likeDao")
public class LikeDaoImpl extends GenericDaoHibernate<Like, Long>
	implements LikeDao {

	public LikeDaoImpl() {
		super(Like.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Like likeComment(Comment comment, User user) {
		Like like = (Like)getSession().createCriteria(Like.class)
						.add(Restrictions.eq("type", Like.C_TYPE_COMMENT))
						.add(Restrictions.eq("comment", comment))
						.add(Restrictions.eq("user", user)).uniqueResult();
		if(null == like) {
			like = new Like();
			like.setType(Like.C_TYPE_COMMENT);
			like.setComment(comment);
			like.setUser(user);
			like.setCreateTime(Calendar.getInstance());
			like = this.save(like);
		}
		return like;
	}

	@Override
	public void unLikeComment(Comment comment, User user) {
		Like like = (Like)getSession().createCriteria(Like.class)
				.add(Restrictions.eq("type", Like.C_TYPE_COMMENT))
				.add(Restrictions.eq("comment", comment))
				.add(Restrictions.eq("user", user)).uniqueResult();
		if(null != like) {
			this.remove(like);
		}
	}

	@Override
	public Like likePhoto(Photo photo, User user) {
		Like like = (Like)getSession().createCriteria(Like.class)
				.add(Restrictions.eq("type", Like.C_TYPE_PHOTO))
				.add(Restrictions.eq("photo", photo))
				.add(Restrictions.eq("user", user)).uniqueResult();
		
		if(null == like) {
			like = new Like();
			like.setType(Like.C_TYPE_PHOTO);
			like.setPhoto(photo);
			like.setUser(user);
			like.setCreateTime(Calendar.getInstance());
			like = this.save(like);
		}
		return like;
	}

	@Override
	public Like likeTravel(Travel travel, User user) {
		Like like = (Like)getSession().createCriteria(Like.class)
				.add(Restrictions.eq("type", Like.C_TYPE_TRAVEL))
				.add(Restrictions.eq("travel", travel))
				.add(Restrictions.eq("user", user)).uniqueResult();
		if(null == like) {
			like = new Like();
			like.setType(Like.C_TYPE_TRAVEL);
			like.setTravel(travel);
			like.setUser(user);
			like.setCreateTime(Calendar.getInstance());
			like = this.save(like);
		}
		return like;
	}

	@Override
	public void unLikePhoto(Photo photo, User user) {
		Like like = (Like)getSession().createCriteria(Like.class)
				.add(Restrictions.eq("type", Like.C_TYPE_PHOTO))
				.add(Restrictions.eq("photo", photo))
				.add(Restrictions.eq("user", user)).uniqueResult();
		
		if(null != like) {
			this.remove(like);
		}
	}

	@Override
	public void unLikeTravel(Travel travel, User user) {
		Like like = (Like)getSession().createCriteria(Like.class)
				.add(Restrictions.eq("type", Like.C_TYPE_TRAVEL))
				.add(Restrictions.eq("travel", travel))
				.add(Restrictions.eq("user", user)).uniqueResult();
		if(null != like) {
			this.remove(like);
		}
	}

	@Override
	public Like getComment(Comment comment, User user) {
		return (Like)getSession().createCriteria(Like.class)
				.add(Restrictions.eq("type", Like.C_TYPE_COMMENT))
				.add(Restrictions.eq("comment", comment))
				.add(Restrictions.eq("user", user)).uniqueResult();
	}

	@Override
	public Like getPhoto(Photo photo, User user) {
		return (Like)getSession().createCriteria(Like.class)
				.add(Restrictions.eq("type", Like.C_TYPE_PHOTO))
				.add(Restrictions.eq("photo", photo))
				.add(Restrictions.eq("user", user)).uniqueResult();
	}

	@Override
	public Like getTravel(Travel travel, User user) {
		return (Like)getSession().createCriteria(Like.class)
				.add(Restrictions.eq("type", Like.C_TYPE_TRAVEL))
				.add(Restrictions.eq("travel", travel))
				.add(Restrictions.eq("user", user)).uniqueResult();
	}
	
}
