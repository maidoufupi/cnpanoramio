package com.cnpanoramio.listener;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.event.internal.DefaultMergeEventListener;
import org.hibernate.event.spi.MergeEvent;
import org.springframework.stereotype.Component;

import com.cnpanoramio.domain.CreateModifiable;

@Component
public class MergeDateListener extends DefaultMergeEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2801232027297432420L;

	@Override
	public void onMerge(MergeEvent event) throws HibernateException {
		if (event.getResult() instanceof CreateModifiable) {
			CreateModifiable record = (CreateModifiable) event.getResult();
			record.setCreateDate(new Date());
			record.setModifyDate(new Date());
		}
		super.onMerge(event);
	}

}
