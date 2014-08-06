package com.cnpanoramio.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.json.TravelResponse.Travel;
import com.cnpanoramio.json.TravelResponse.TravelSpot;
import com.cnpanoramio.service.TravelManager;
import com.cnpanoramio.service.TravelService;
import com.cnpanoramio.utils.PhotoUtil;
import com.cnpanoramio.utils.UserUtil;

@Service
@Transactional
public class TravelServiceImpl implements TravelService {

	@Autowired
	private UserManager userManager;
	
	@Autowired
	private TravelManager travelManager;
	
	@Override
	public Travel getTravel(Long travelId) {
		return convertTravel(travelManager.getTravel(travelId));
	}

	@Override
	public List<Travel> getTravels(User user) {
		List<com.cnpanoramio.domain.Travel> travels = travelManager.getTravels(user);
		List<Travel> ts = convertTravels(travels);
		for(Travel travel : ts) {
			travel.setSpots(null);
		}
		return ts;
	}

	@Override
	public Travel createMyTravel(User user, String travel) {
		com.cnpanoramio.domain.Travel tra = travelManager.createMyTravel(user, travel);
		return convertTravel(tra);
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
		BeanUtils.copyProperties(travel, t, new String[]{"spots", "user", "spot", "albumCover"});
		if(null != travel.getUser()) {
			t.setUserId(travel.getUser().getId());
			t.setUsername(travel.getUser().getName());
			t.setUser(UserUtil.getSimpleOpenInfo(travel.getUser()));
		}
		if(null != travel.getSpot()) {
			t.setSpot(convertTravelSpot(travel.getSpot()));
		}
		for(com.cnpanoramio.domain.TravelSpot travelSpot : travel.getSpots()) {
			t.getSpots().add(convertTravelSpot(travelSpot));
		}
		
		// set album cover photo
		if(null != travel.getAlbumCover()) {
			t.setAlbumCover(PhotoUtil.getPhotoOssKey(travel.getAlbumCover()));
		}else {
			Iterator<com.cnpanoramio.domain.TravelSpot> iter = travel.getSpots().iterator();
			if(iter.hasNext()) {
				com.cnpanoramio.domain.TravelSpot spot = iter.next();
				Iterator<Photo> iterPhotos = spot.getPhotos().iterator();
				if(iterPhotos.hasNext()) {
					Photo photo = iterPhotos.next();
					t.setAlbumCover(PhotoUtil.getPhotoOssKey(photo));					
				}
				
			}
					
		}
		
		// photo size
		Integer photoSize = 0;
		for(com.cnpanoramio.domain.TravelSpot spot : travel.getSpots()) {
			photoSize += spot.getPhotos().size();
		}
		t.setPhotoSize(photoSize);
		
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

	@Override
	public TravelSpot changeSpot(Long id, TravelSpot spot) {
		
		com.cnpanoramio.domain.TravelSpot travelSpot = new com.cnpanoramio.domain.TravelSpot();
		travelSpot.setAddress(spot.getAddress());
		travelSpot.setTitle(spot.getTitle());
		travelSpot.setDescription(spot.getDescription());
		travelSpot.setTimeStart(spot.getTimeStart());
		travelSpot = travelManager.changeSpot(id, travelSpot);
		return convertTravelSpot(travelSpot);
	}

	@Override
	public Travel changeTravelDesc(Long id, String description) {
		com.cnpanoramio.domain.Travel travel = travelManager.getTravel(id);
		User me = UserUtil.getCurrentUser(userManager);
		if(!travel.getUser().getId().equals(me.getId())) {
			throw new AccessDeniedException("Travel (id=" + id + ") is not belong to you");
		}
		return convertTravel(travelManager.changeTravelDesc(id, description));
	}

	@Override
	public TravelSpot getSpot(Long id) {
		return convertTravelSpot(travelManager.getTravelSpot(id));
	}

	@Override
	public Travel addTravelPhotos(Long id, List<Long> photos) {
		return convertTravel(travelManager.addTravelPhotos(id, photos));
	}

	@Override
	public Travel removeTravelPhotos(Long id, List<Long> photos) {
		return convertTravel(travelManager.removeTravelPhotos(id, photos));
	}

	@Override
	public TravelSpot createTravelSpot(Long id, TravelSpot spot) {
		com.cnpanoramio.domain.TravelSpot travelSpot = new com.cnpanoramio.domain.TravelSpot();
		travelSpot.setAddress(spot.getAddress());
		travelSpot.setTitle(spot.getTitle());
		travelSpot.setDescription(spot.getDescription());
		travelSpot.setTimeStart(spot.getTimeStart());
		return convertTravelSpot(travelManager.createTravelSpot(id, travelSpot));
	}

	@Override
	public Travel addSpotPhotos(Long id, List<Long> photos) {
		return convertTravel(travelManager.addSpotPhotos(id, photos));
	}

	@Override
	public Travel deleteSpot(Long id) {
		return convertTravel(travelManager.deleteSpot(id));
	}

	@Override
	public void deleteTravel(Long id) {
		travelManager.deleteTravel(id);
	}

	@Override
	public void removeTravel(Long id) {
		travelManager.removeTravel(id);		
	}

	@Override
	public void cancelDeleteTravel(Long id) {
		travelManager.cancelDeleteTravel(id);		
	}

}
