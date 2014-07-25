package com.cnpanoramio.dao.impl.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.appfuse.model.User;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.TravelDao;
import com.cnpanoramio.domain.Travel;
import com.cnpanoramio.domain.UserSettings;

@Repository("travelDao")
@Transactional
public class TravelDaoImpl extends GenericDaoHibernate<Travel, Long> implements
		TravelDao {

	public TravelDaoImpl() {
		super(Travel.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Travel getByName(Long userId, String name) {
		return (Travel) getSession().createCriteria(Travel.class)
				.add(Restrictions.eq("user.id", userId))
				.add(Restrictions.eq("title", name)).uniqueResult();
	}
	
	public List<Travel> searchTravel(String term) {
		return null;
	}

}
