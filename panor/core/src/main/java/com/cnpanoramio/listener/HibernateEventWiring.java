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
		registry.prependListeners(EventType.MERGE, mListener);
		registry.prependListeners(EventType.SAVE_UPDATE, saveUpdateListener);
		registry.prependListeners(EventType.SAVE, saveUpdateListener);
	}

}
