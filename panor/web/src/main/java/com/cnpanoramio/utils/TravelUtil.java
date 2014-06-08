package com.cnpanoramio.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.json.TravelResponse;
import com.cnpanoramio.json.TravelResponse.Travel;
import com.cnpanoramio.json.TravelResponse.TravelSpot;

public class TravelUtil {
	
//	public static List<Travel> convertTravels(List<com.cnpanoramio.domain.Travel> travels) {
//		List<Travel> ts = new ArrayList<Travel>();
//		for(com.cnpanoramio.domain.Travel t : travels) {
//			ts.add(convertTravel(t));
//		}
//		return ts;
//	}
//
//	public static Travel convertTravel(com.cnpanoramio.domain.Travel travel) {
//		Travel t = new Travel();
//		BeanUtils.copyProperties(travel, t, new String[]{"spots", "user"});
//		t.setUserId(travel.getUser().getId());
//		if(null != travel.getSpot()) {
//			t.setSpotId(travel.getSpot().getId());			
//		}
//		for(com.cnpanoramio.domain.TravelSpot travelSpot : travel.getSpots()) {
//			t.getSpots().add(convertTravelSpot(travelSpot));
//		}
//		return t;
//	}
//	
//	public static TravelSpot convertTravelSpot(com.cnpanoramio.domain.TravelSpot travelSpot) {
//		TravelSpot tSpot = new TravelSpot();
//		BeanUtils.copyProperties(travelSpot, tSpot, new String[]{"travel", "photos"});
//		tSpot.setTravelId(travelSpot.getTravel().getId());
//		for(Photo photo : travelSpot.getPhotos()) {
//			tSpot.getPhotos().add(PhotoUtil.transformProperties(photo));
//		}
//		return tSpot;
//	}
}
