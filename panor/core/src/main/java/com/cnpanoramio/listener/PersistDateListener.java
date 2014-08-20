package com.cnpanoramio.listener;

import java.util.Date;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.event.internal.DefaultPersistEventListener;
import org.hibernate.event.spi.PersistEvent;
import org.springframework.stereotype.Component;

import com.cnpanoramio.domain.CreateModifiable;

@Component
public class PersistDateListener extends DefaultPersistEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8535417489384698056L;

	@Override
	public void onPersist(PersistEvent event)
			throws HibernateException {
		if (event.getObject() instanceof CreateModifiable) {
			CreateModifiable record = (CreateModifiable) event.getObject();
			record.setCreateDate(new Date());
			record.setModifyDate(new Date());
		}
		super.onPersist(event);
	}
}
