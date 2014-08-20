package com.cnpanoramio.dao.impl.hibernate;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.dao.MessageDao;
import com.cnpanoramio.domain.Message;

@Repository("messageDao")
public class MessageDaoImpl extends GenericDaoHibernate<Message, Long> implements MessageDao{

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

}
