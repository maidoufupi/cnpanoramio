package com.cnpanoramio.dao.impl.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.appfuse.model.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.dao.MessageQueueDao;
import com.cnpanoramio.domain.Message;
import com.cnpanoramio.domain.MessageQueue;

@Repository("messageQueueDao")
public class MessageQueueDaoImpl extends GenericDaoHibernate<MessageQueue, Long> implements MessageQueueDao {

	public MessageQueueDaoImpl() {
		super(MessageQueue.class);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public MessageQueue persist(MessageQueue messageQueue) {
		getSession().persist(messageQueue);
		return messageQueue;
	}

	@Override
	public MessageQueue getMessageQueue(User user, Message message) {
		return (MessageQueue)getSession().createCriteria(MessageQueue.class)
				.add(Restrictions.eq("user", user))
				.add(Restrictions.eq("message", message)).uniqueResult();
	}

	@Override
	public List<MessageQueue> getMessageQueueList(User user, int pageSize,
			int pageNo) {
		Criteria criteria = getSession().createCriteria(MessageQueue.class)
			.add(Restrictions.eq("user", user))
			.add(Restrictions.eq("deleted", false))
			.addOrder(Order.desc("modifyDate"));
		
		criteria.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize);
		  
		return criteria.list();
	}

	@Override
	public List<MessageQueue> getMessageQueueListByMessage(Message message) {
		
		Criteria criteria = getSession().createCriteria(MessageQueue.class)
				.add(Restrictions.eq("message", message))
				.addOrder(Order.desc("modifyDate"));
		
		return criteria.list();
	}

	

}
