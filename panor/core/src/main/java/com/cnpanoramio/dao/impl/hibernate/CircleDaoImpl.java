package com.cnpanoramio.dao.impl.hibernate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.appfuse.model.User;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.dao.CircleDao;
import com.cnpanoramio.domain.Circle;
import com.cnpanoramio.domain.Message;

@Repository("circleDao")
public class CircleDaoImpl extends GenericDaoHibernate<Circle, Long> implements
		CircleDao {

	public CircleDaoImpl() {
		super(Circle.class);
	}

	@Override
	public Circle persist(Circle circle) {
		getSession().persist(circle);
		return circle;
	}

	@Override
	public List<Circle> getUserCircles(User user) {
		return getSession().createCriteria(Circle.class)
				.add(Restrictions.eq("owner", user)).list();
	}

	@Override
	public Circle getUserCircleByName(User user, String name) {
		return (Circle) getSession().createCriteria(Circle.class)
				.add(Restrictions.eq("owner", user))
				.add(Restrictions.eq("name", name)).uniqueResult();
	}

	@Override
	public List<User> getFollowSuggested(User user, int pageSize, int pageNo) {
		// SQLQuery query =
		// getSession().createSQLQuery("select u.id from app_user u inner join circle c on u.id = c.owner_id inner join circle_users cu on c.id = cu.circle_id where u.id not in (select cu.circle_id from app_user u inner join circle c on u.id = c.owner_id inner join circle_users cu on c.id = cu.circle_id where u.id = :id) group by u.id order by count(*) DESC");
		Query query = getSession()
				.createQuery(
						"select u from User u, Circle c inner join c.users cu "
						+ "where u.id <> :id and c.owner = u and u not in "
						+ "(select cu from User u, Circle c join c.users cu where u.id = :id and c.owner = u ) "
						+ "group by u order by count(cu) desc");

		query.setParameter("id", user.getId());
		query.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize);
		return query.list();
	}
}
