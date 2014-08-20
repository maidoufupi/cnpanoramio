package com.cnpanoramio.dao.impl.hibernate;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.appfuse.model.User;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.dao.CircleDao;
import com.cnpanoramio.domain.Circle;

@Repository("circleDao")
public class CircleDaoImpl extends GenericDaoHibernate<Circle, Long> 
	implements CircleDao {

	public CircleDaoImpl() {
		super(Circle.class);
	}
	
}
