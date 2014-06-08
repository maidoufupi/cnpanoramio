package com.cnpanoramio.dao.impl.hibernate;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.dao.TravelSpotDao;
import com.cnpanoramio.domain.Travel;
import com.cnpanoramio.domain.TravelSpot;

@Repository("travelSpotDao")
public class TravelSpotDaoImpl extends GenericDaoHibernate<TravelSpot, Long>
		implements TravelSpotDao {

	public TravelSpotDaoImpl() {
		super(TravelSpot.class);
		// TODO Auto-generated constructor stub
	}

}
