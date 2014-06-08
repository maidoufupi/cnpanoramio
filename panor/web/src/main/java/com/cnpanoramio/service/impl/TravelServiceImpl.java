package com.cnpanoramio.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.json.TravelResponse.Travel;
import com.cnpanoramio.json.TravelResponse.TravelSpot;
import com.cnpanoramio.service.TravelManager;
import com.cnpanoramio.service.TravelService;
import com.cnpanoramio.utils.PhotoUtil;

@Service
@Transactional
public class TravelServiceImpl implements TravelService {

	@Autowired
	private TravelManager travelManager;
	
	@Override
	public Travel getTravel(Long travelId) {
		return convertTravel(travelManager.getTravel(travelId));
	}

	@Override
	public List<Travel> getTravels(User user) {
		List<com.cnpanoramio.domain.Travel> travels = travelManager.getTravels(user);
		return convertTravels(travels);
	}

	@Override
	public List<Travel> createMyTravel(User user, String travel) {
		List<com.cnpanoramio.domain.Travel> travels = travelManager.createMyTravel(user, travel);
		return convertTravels(travels);
	}
	
	public static List<Travel> convertTravels(List<com.cnpanoramio.domain.Travel> travels) {
		List<Travel> ts = new ArrayList<Travel>();
		for(com.cnpanoramio.domain.Travel t : travels) {
			ts.add(convertTravel(t));
		}
		return ts;
	}

	public static Travel convertTravel(com.cnpanoramio.domain.Travel travel) {
		Travel t = new Travel();
		BeanUtils.copyProperties(travel, t, new String[]{"spots", "user", "spot"});
		if(null != travel.getUser()) {
			t.setUserId(travel.getUser().getId());
		}
		if(null != travel.getSpot()) {
			t.setSpot(convertTravelSpot(travel.getSpot()));
		}
		for(com.cnpanoramio.domain.TravelSpot travelSpot : travel.getSpots()) {
			t.getSpots().add(convertTravelSpot(travelSpot));
		}
		return t;
	}
	
	public static TravelSpot convertTravelSpot(com.cnpanoramio.domain.TravelSpot travelSpot) {
		TravelSpot tSpot = new TravelSpot();
		BeanUtils.copyProperties(travelSpot, tSpot, new String[]{"travel", "photos"});
		tSpot.setTravelId(travelSpot.getTravel().getId());
		for(Photo photo : travelSpot.getPhotos()) {
			tSpot.getPhotos().add(PhotoUtil.transformProperties(photo));
		}
		return tSpot;
	}

}
