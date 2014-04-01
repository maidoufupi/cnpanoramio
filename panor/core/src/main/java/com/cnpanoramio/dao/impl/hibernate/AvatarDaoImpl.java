package com.cnpanoramio.dao.impl.hibernate;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.appfuse.model.User;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.dao.AvatarDao;
import com.cnpanoramio.domain.Avatar;
import com.cnpanoramio.domain.Favorite;

@Repository("avatarDao")
public class AvatarDaoImpl extends GenericDaoHibernate<Avatar, Long> implements AvatarDao {

	public AvatarDaoImpl() {
		super(Avatar.class);
		// TODO Auto-generated constructor stub
	}
}
