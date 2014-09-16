package com.cnpanoramio.dao;

import org.appfuse.dao.GenericDao;

import com.cnpanoramio.domain.TravelSpot;

public interface TravelSpotDao extends GenericDao<TravelSpot, Long> {
	
	public TravelSpot persist(TravelSpot travelSpot);
	
}
