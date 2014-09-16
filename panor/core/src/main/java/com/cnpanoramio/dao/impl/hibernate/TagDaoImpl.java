package com.cnpanoramio.dao.impl.hibernate;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.appfuse.model.User;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.dao.TagDao;
import com.cnpanoramio.domain.Tag;
import com.cnpanoramio.domain.UserSettings;

@Repository
public class TagDaoImpl extends GenericDaoHibernate<Tag, Long> implements TagDao {

	public TagDaoImpl() {
		super(Tag.class);
	}

	@Override
	public Tag getOrCreateUserTag(UserSettings user, String content) {
		Tag tagObj = (Tag)getSession().createCriteria(Tag.class)
				.add(Restrictions.eq("user", user))
				.add(Restrictions.eq("content", content)).uniqueResult();
		if(null == tagObj) {
			tagObj = new Tag(content);
			tagObj.setUser(user);
			tagObj = save(tagObj);
		}
		return tagObj;
	}

}
