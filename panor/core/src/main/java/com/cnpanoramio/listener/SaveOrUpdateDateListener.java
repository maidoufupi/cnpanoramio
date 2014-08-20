package com.cnpanoramio.listener;

import java.util.Date;

import org.hibernate.event.internal.DefaultSaveOrUpdateEventListener;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.stereotype.Component;

import com.cnpanoramio.domain.CreateModifiable;

@Component
public class SaveOrUpdateDateListener extends DefaultSaveOrUpdateEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6807895569425874784L;

	@Override
	public void onSaveOrUpdate(SaveOrUpdateEvent event) {
		if (event.getObject() instanceof CreateModifiable) {
			CreateModifiable record = (CreateModifiable) event.getObject();
			record.setModifyDate(new Date());
		}
		super.onSaveOrUpdate(event);
	}
}
