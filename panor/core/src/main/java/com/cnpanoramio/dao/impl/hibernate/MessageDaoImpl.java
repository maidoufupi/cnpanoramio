package com.cnpanoramio.dao.impl.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.appfuse.model.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.dao.MessageDao;
import com.cnpanoramio.domain.Circle;
import com.cnpanoramio.domain.Message;
import com.cnpanoramio.domain.MessageQueue;

@Repository("messageDao")
public class MessageDaoImpl extends GenericDaoHibernate<Message, Long> implements MessageDao {

	public MessageDaoImpl() {
		super(Message.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Message getMessage(Message.MessageType type, Long id) {
		return (Message)getSession().createCriteria(Message.class)
				.add(Restrictions.eq("type", type))
				.add(Restrictions.eq("entityId", id)).uniqueResult();
	}

	@Override
	public Message persist(Message message) {
		getSession().persist(message);
		return message;
	}

	@Override
	public List<Message> getUserMessages(User user, int pageSize, int pageNo) {
		Criteria criteria = getSession().createCriteria(Message.class)
				.add(Restrictions.eq("user", user))
				.add(Restrictions.eq("deleted", false))
				.addOrder(Order.desc("modifyDate"));
			
		criteria.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize);
		  
		return criteria.list();
	}

	@Override
	public List<Message> getMessages(User user, int pageSize, int pageNo) {
		
		DetachedCriteria subquery = DetachedCriteria.forClass(Circle.class, "circle")
				.createAlias("circle.users", "user")
		    .add(Restrictions.eq("owner", user))
		    .setProjection(Projections.property("user.id"));
		    
		Criteria criteria = getSession().createCriteria(Message.class, "message")
				.createAlias("message.user", "user")
			.add(Subqueries.propertyIn("user.id", subquery))
			.addOrder(Order.desc("modifyDate"));

		criteria.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize);
		
		return criteria.list();
	}

}
