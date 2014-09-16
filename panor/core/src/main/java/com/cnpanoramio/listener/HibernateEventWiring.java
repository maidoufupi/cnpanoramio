package com.cnpanoramio.listener;

import javax.annotation.PostConstruct;

import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HibernateEventWiring {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private PersistDateListener persistListener;
	@Autowired
	private SaveOrUpdateDateListener saveUpdateListener;
	@Autowired
	private MergeDateListener mListener;
	
	@PostConstruct
	public void registerListeners() {
		EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory)
				.getServiceRegistry().getService(EventListenerRegistry.class);
		registry.prependListeners(EventType.PERSIST, persistListener);
		// MERGE 加上会有问题 会持久化两个一样的实体 未解决; 持久化时要使用persist
//		registry.prependListeners(EventType.MERGE, mListener);
		registry.prependListeners(EventType.SAVE_UPDATE, saveUpdateListener);
		registry.prependListeners(EventType.SAVE, saveUpdateListener);
	}

}
